package ch.heigvd.pro.b04.android.Poll

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.pro.b04.android.Datamodel.Question
import ch.heigvd.pro.b04.android.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import java.util.*

class PollAdapter(private val state: PollViewModel,
                  private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var questions: List<Question> = LinkedList()
    override fun getItemId(position: Int): Long {
        return if (position == 0) HEADER_ID else questions[position - 1].idQuestion
    }

    private class HeaderViewHolder(parent: ViewGroup,
                                   state: PollViewModel,
                                   lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.poll_title, parent, false)) {

        private val title: TextView = itemView.findViewById(R.id.poll_title)

        init {
            lifecycleOwner.lifecycleScope.launchWhenStarted {
                state.poll.collect { poll -> title.text = poll.title }
            }
        }
    }

    private inner class QuestionViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.poll_question, parent, false)) {

        private val questionButton: Button = itemView.findViewById(R.id.poll_question_item)

        fun bindQuestion(question: Question, answered: Boolean) {
            questionButton.text = question.title
            questionButton.setOnClickListener { state.goToQuestion(question) }
            if (answered) {
                questionButton.setBackgroundColor(Color.GREEN)
            } else {
                questionButton.setBackgroundColor(Color.WHITE)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent, state, lifecycleOwner)
            VIEW_TYPE_QUESTION -> QuestionViewHolder(parent)
            else -> throw IllegalStateException("Unknown view type.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != 0) {
            val q = questions[position - 1]
            (holder as QuestionViewHolder).bindQuestion(q, q.answered())
        }
    }

    override fun getItemCount(): Int {
        return questions.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_QUESTION
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_QUESTION = 1
        private const val HEADER_ID: Long = -1
    }

    init {
        setHasStableIds(true)
        lifecycleOwner.lifecycleScope.launchWhenStarted {
            state.questions
                    .map { it.sortedBy { question -> question.indexInPoll } }
                    .collect {
                        questions = it
                        notifyDataSetChanged()
                    }
        }
    }
}