package ch.heigvd.pro.b04.android.question

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pro.b04.android.R
import ch.heigvd.pro.b04.android.datamodel.Answer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class QuestionAdapter(
        private val state: QuestionViewModel,
        private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var answers: List<Answer> = LinkedList()
    private var title: String = ""

    override fun getItemId(position: Int): Long {
        return if (position == 0) HEADER_ID else answers[position - 1].idAnswer
    }

    private class HeaderViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.question_title, parent, false)) {

        private val title: TextView = itemView.findViewById(R.id.question)

        fun bindTitle(text: String) {
            title.text = text
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
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent)
            VIEW_TYPE_ANSWER -> AnswerViewHolder(parent)
            else -> throw IllegalStateException("Unknown view type.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            (holder as HeaderViewHolder).bindTitle(title)
        }
        if (position != 0) {
            (holder as AnswerViewHolder).bindAnswer(answers[position - 1])
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
        state.viewModelScope.launch {
            state.answers.collect {
                this@QuestionAdapter.answers = it
                notifyDataSetChanged()
            }
        }
        state.viewModelScope.launch {
            state.currentQuestion.collect {
                this@QuestionAdapter.title = it.title
                notifyItemChanged(0)
            }
        }
    }
}