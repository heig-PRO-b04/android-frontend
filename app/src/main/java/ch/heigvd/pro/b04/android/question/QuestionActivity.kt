package ch.heigvd.pro.b04.android.question

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pro.b04.android.R
import ch.heigvd.pro.b04.android.authentication.AuthenticationTokenLiveData
import ch.heigvd.pro.b04.android.datamodel.Question
import ch.heigvd.pro.b04.android.network.NetworkError
import ch.heigvd.pro.b04.android.poll.PollActivity
import kotlinx.coroutines.flow.collect

class QuestionActivity : AppCompatActivity() {
    private lateinit var state: QuestionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_activity)
        val intent = intent
        val question = intent.getSerializableExtra(PollActivity.EXTRA_QUESTION) as Question
        val token = getIntent().getStringExtra(PollActivity.EXTRA_TOKEN) ?: "empty"

        state = ViewModelProvider(this, QuestionViewModelFactory(
            application,
            question,
            token
        )).get(QuestionViewModel::class.java)

        // Setup list of answers
        val answerList = findViewById<RecyclerView>(R.id.question_answers_view)
        val manager = LinearLayoutManager(this)
        val questionAdapter = QuestionAdapter(state, this)

        answerList.itemAnimator = DefaultItemAnimator()
        answerList.adapter = questionAdapter
        answerList.layoutManager = manager

        // Setup button behaviour
        val alert = findViewById<TextView>(R.id.question_answers_alert)
        val beforeButton = findViewById<ImageButton>(R.id.before_button)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val nextButton = findViewById<ImageButton>(R.id.next_button)

        beforeButton.setOnClickListener { goBack() }
        backButton.setOnClickListener { finish() }
        nextButton.setOnClickListener { goNext() }

        // React to change in state
        lifecycleScope.launchWhenStarted {
            state.networkErrors().collect {
                if (it == NetworkError.TokenNotValid)
                    disconnect()
            }
        }

        lifecycleScope.launchWhenStarted {
            state.getMinCheckedAnswers().collect { votes ->
                if (votes != null) {
                    alert.text = resources.getString(R.string.question_answers_min_alerts, votes)
                    alert.visibility = View.VISIBLE
                } else {
                    alert.visibility = View.INVISIBLE
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            state.nextButtonVisible.collect { visible ->
                if (visible)
                    nextButton.visibility = View.VISIBLE
                else
                    nextButton.visibility = View.INVISIBLE
            }
        }

        lifecycleScope.launchWhenStarted {
            state.previousButtonVisible.collect { visible ->
                if (visible)
                    beforeButton.visibility = View.VISIBLE
                else
                    beforeButton.visibility = View.INVISIBLE
            }
        }

        lifecycleScope.launchWhenStarted {
            state.tooManyAnswers.collect {
                Toast.makeText(
                        applicationContext,
                        resources.getQuantityString(R.plurals.question_answers_max_toast, it, it),
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun goBack() {
        state.changeToPreviousQuestion()
    }

    private fun goNext() {
        state.changeToNextQuestion()
    }

    private fun disconnect() {
        val tokenLiveData = AuthenticationTokenLiveData(applicationContext)
        tokenLiveData.logout()
        finish()
    }
}