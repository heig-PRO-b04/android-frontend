package ch.heigvd.pro.b04.android.question

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect

class QuestionActivity : AppCompatActivity() {
    private lateinit var state: QuestionViewModel

    @OptIn(InternalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        val intent = intent
        val question = intent.getSerializableExtra(PollActivity.EXTRA_QUESTION) as Question
        val token = getIntent().getStringExtra(PollActivity.EXTRA_TOKEN)

        state = ViewModelProvider(this, QuestionViewModelFactory(
            application,
            question,
            token
        )).get(QuestionViewModel::class.java)

        setupAnswerMinAlert(question)
        setupAnswerList()

        lifecycleScope.launchWhenStarted {
            state.networkErrors().collect {
                if (it == NetworkError.TokenNotValid)
                    disconnect()
            }
        }
    }

    private fun setupAnswerList() {
        val answerList = findViewById<RecyclerView>(R.id.question_answers_view)
        val manager = LinearLayoutManager(this)
        val questionAdapter = QuestionAdapter(state, this)

        answerList.itemAnimator = DefaultItemAnimator()
        answerList.adapter = questionAdapter
        answerList.layoutManager = manager
    }

    private fun setupAnswerMinAlert(question: Question) {
        val alert = findViewById<TextView>(R.id.question_answers_alert)
        state.getNbCheckedAnswer().observe(this, Observer { nbrVotes: Int ->
            if (question.answerMin < nbrVotes) {
                alert.setText(R.string.answers_min_alerts)
            } else {
                alert.text = ""
            }
        })
    }

    fun goBack(view: View?) {
        state.changeToPreviousQuestion()
    }

    fun goNext(view: View?) {
        state.changeToNextQuestion()
    }

    fun exitQuestion(view: View?) {
        finish()
    }

    private fun disconnect() {
        val tokenLiveData = AuthenticationTokenLiveData(applicationContext)
        tokenLiveData.logout()
        finish()
    }
}