package ch.heigvd.pro.b04.android.Question

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pro.b04.android.Datamodel.Answer
import ch.heigvd.pro.b04.android.Datamodel.Question
import ch.heigvd.pro.b04.android.R
import kotlinx.coroutines.flow.collect
import java.util.*

class QuestionAdapter(private val state: QuestionViewModel,
                      private val lifecycleOwner: LifecycleOwner,
                      private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var answers: List<Answer> = LinkedList()

    override fun getItemId(position: Int): Long {
        return if (position == 0) HEADER_ID else answers[position - 1].idAnswer
    }

    private class HeaderViewHolder(parent: ViewGroup,
                                   state: QuestionViewModel,
                                   lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)

        .inflate(R.layout.question_title, parent, false)) {
        private val title: TextView = itemView.findViewById(R.id.question)

        init {
            state.currentQuestion.observe(lifecycleOwner, Observer {
                selectedQuestion: Question -> title.text = selectedQuestion.title
            })
        }
    }

    private inner class AnswerViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.question_answers, parent, false)) {

        private val answerButton: Button = itemView.findViewById(R.id.question_answer_item)

        fun bindAnswer(answer: Answer) {
            var text = answer.title
            if (answer.description != "") {
                text += " : " + answer.description
            }
            answerButton.text = text
            updateButtonColor(answer)

            answerButton.setOnClickListener {
                state.selectAnswer(answer)
                updateButtonColor(answer)
            }
        }

        private fun updateButtonColor(answer: Answer) {
            val color = if (answer.isChecked) context.getColor(R.color.colorSelected) else Color.WHITE
            answerButton.setBackgroundColor(color)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent, state, lifecycleOwner)
            VIEW_TYPE_ANSWER -> AnswerViewHolder(parent)
            else -> throw IllegalStateException("Unknown view type.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != 0) {
            (holder as AnswerViewHolder)
                .bindAnswer(answers[position - 1])
        }
    }

    override fun getItemCount(): Int {
        return answers.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ANSWER
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ANSWER = 1
        private const val HEADER_ID: Long = -1
    }

    init {
        setHasStableIds(true)
        lifecycleOwner.lifecycleScope.launchWhenStarted {
            state.answers.collect {
                this@QuestionAdapter.answers = it
                notifyDataSetChanged()
            }
        }
    }
}