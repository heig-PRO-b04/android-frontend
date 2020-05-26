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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class QuestionViewModel(application: Application, question : Question, private val token : String)
        : RequestsViewModel(application, question.idModerator.toInt(), question.idPoll.toInt(), token) {

    private val previousQuestion : MutableStateFlow<Long> = MutableStateFlow(System.currentTimeMillis())
    private val nextQuestion : MutableStateFlow<Long> = MutableStateFlow(System.currentTimeMillis())

    private var lastVoteAtTime : Long = System.currentTimeMillis()

    private val pollState: PollState = PollState(
        viewModelScope,
        Poll(question.idModerator.toInt(), question.idPoll.toInt()),
        question,
        token,
        nextQuestion.map{},
        previousQuestion.map{},
        emptyFlow()
    )

    val currentQuestion : Flow<Question> = pollState.data.map { it.current }
    val answers: Flow<List<Answer>> = pollState.data.map { it.map[it.current]?.map { it.answer } ?: emptyList()}

    init {
        /*
        viewModelScope.launch {
            answers.map {
                it.filter { it.isChecked }.size
            }.collect { nbCheckedAnswer.value = it }
        }
         */
    }

    fun selectAnswer(answer: Answer) {
        /*
        val question: Question? = currentQuestion.value

        if (question == null || question.idQuestion != answer.idQuestion)
            return

        val max = if (question.answerMax < question.answerMin) 0 else question.answerMax

        if (answer.isChecked || max > nbCheckedAnswer.value || max == 0) {
            lastVoteAtTime = System.currentTimeMillis()

            if (answer.isChecked) nbCheckedAnswer.value-- else nbCheckedAnswer.value++

            answer.toggle()
            // Note that for now, we do not take the result into account
            viewModelScope.launch {
                voteForAnswerSuspending(answer, token)
            }
        } else if (max != 0 && max == nbCheckedAnswer.value) {
            notifyMaxAnswer.value = question.answerMax

            // Not useless: if we do not set the value back to 0, the activity will not be
            // notified if the user clicks multiple times on an answer
            notifyMaxAnswer.value = 0
        }
        */
    }

    fun changeToPreviousQuestion() : Unit {
        previousQuestion.value = System.currentTimeMillis()
    }

    fun changeToNextQuestion() : Unit {
        nextQuestion.value = System.currentTimeMillis()
    }

    fun getNbCheckedAnswer() : Flow<Int> {
        return pollState.nbCheckedAnswer
    }

    /*
    fun notifyMaxAnswers() : flow<Int> {
        return notifyMaxAnswer
    }
     */
}