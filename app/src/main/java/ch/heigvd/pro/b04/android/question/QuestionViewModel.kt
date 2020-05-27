package ch.heigvd.pro.b04.android.question

import android.app.Application
import androidx.lifecycle.viewModelScope
import ch.heigvd.pro.b04.android.datamodel.Answer
import ch.heigvd.pro.b04.android.datamodel.Poll
import ch.heigvd.pro.b04.android.datamodel.Question
import ch.heigvd.pro.b04.android.network.RequestsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class QuestionViewModel(
        application: Application,
        question: Question,
        token: String
) : RequestsViewModel(
        application,
        question.idModerator.toInt(),
        question.idPoll.toInt(),
        token
) {

    private val previousQuestion: MutableStateFlow<Long> = MutableStateFlow(System.currentTimeMillis())
    private val nextQuestion: MutableStateFlow<Long> = MutableStateFlow(System.currentTimeMillis())
    private val votes: MutableStateFlow<Pair<Long, Answer?>> = MutableStateFlow(System.currentTimeMillis() to null)

    private val pollState: PollState = PollState(
            viewModelScope,
            Poll(question.idModerator.toInt(), question.idPoll.toInt()),
            question,
            token,
            nextQuestion.map {},
            previousQuestion.map {},
            votes.map { (_, answer) -> answer }.filterNotNull()
    )

    val currentQuestion: Flow<Question> = pollState.data.map { it.current }
    val answers: Flow<List<Answer>> = pollState.data.map {
        it.map[it.current]?.map { it.answer } ?: emptyList()
    }

    fun selectAnswer(answer: Answer) {
        votes.value = System.currentTimeMillis() to answer
    }

    fun changeToPreviousQuestion() {
        previousQuestion.value = System.currentTimeMillis()
    }

    fun changeToNextQuestion() {
        nextQuestion.value = System.currentTimeMillis()
    }

    fun getMinCheckedAnswers(): Flow<Int?> {
        return pollState.minCheckedAnswers
    }

    val nextButtonVisible : Flow<Boolean>
        get() = pollState.nextButtonVisible

    val previousButtonVisible : Flow<Boolean>
        get() = pollState.previousButtonVisible

    val tooManyAnswers : Flow<Int>
        get() = pollState.tooManyAnswers
}