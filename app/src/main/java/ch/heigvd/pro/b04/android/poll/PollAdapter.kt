package ch.heigvd.pro.b04.android.poll

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pro.b04.android.R
import ch.heigvd.pro.b04.android.datamodel.Question
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

@FlowPreview
class PollAdapter(private val state: PollViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var questions: List<Question> = LinkedList()
    override fun getItemId(position: Int): Long {
        return when (position) {
            0 -> HEADER_ID
            1 -> INSTRUCTION_ID
            else -> questions[position - 2].idQuestion
        }
    }

    private class HeaderViewHolder(parent: ViewGroup,
                                   state: PollViewModel
    ) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.poll_title, parent, false)) {

        private val title: TextView = itemView.findViewById(R.id.poll_title)

        init {
            state.viewModelScope.launch {
                state.poll.collect { poll -> title.text = poll.title }
            }
        }
    }

    private class InstructionViewHolder(parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.home_instructions, parent, false)) {
        private val title : TextView = itemView.findViewById(R.id.home_instructions_title)

        init {
            title.text = parent.context.getString(R.string.poll_instructions)
        }
    }

    @FlowPreview
    private inner class QuestionViewHolder(private val parent: ViewGroup)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.poll_question, parent, false)) {

        private val questionButton: Button = itemView.findViewById(R.id.poll_question_item)

        fun bindQuestion(question: Question) {

            questionButton.setOnClickListener { state.goToQuestion(question) }

            var text = question.title

            if (question.details != null && question.details != "") {
                text += "\n" + question.details
            }

            val spannable = SpannableString(text)
            spannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, question.title.length, 0 )

            if (question.details != null && question.details != "") {
                spannable.setSpan(
                    ForegroundColorSpan(ContextCompat
                        .getColor(parent.context, R.color.colorDescription)),
                    question.title.length + 1,
                    text.length,
                    0
                )
            }

            questionButton.text = spannable
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent, state)
            VIEW_TYPE_INSTRUCTION -> InstructionViewHolder(parent)
            VIEW_TYPE_QUESTION -> QuestionViewHolder(parent)
            else -> throw IllegalStateException("Unknown view type.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position > 1) {
            val q = questions[position - 2]
            (holder as QuestionViewHolder).bindQuestion(q)
        }
    }

    override fun getItemCount(): Int {
        return questions.size + 2
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> VIEW_TYPE_HEADER
            1 -> VIEW_TYPE_INSTRUCTION
            else -> VIEW_TYPE_QUESTION
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_QUESTION = 1
        private const val VIEW_TYPE_INSTRUCTION = 2
        private const val HEADER_ID: Long = -1
        private const val INSTRUCTION_ID: Long = -2
    }

    init {
        setHasStableIds(true)
        state.viewModelScope.launch {
            state.questions
                .map { it.sortedBy { question -> question.indexInPoll } }
                .collect {
                    questions = it
                    notifyDataSetChanged()
                }
        }
    }
}