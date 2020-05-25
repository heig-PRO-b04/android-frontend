package ch.heigvd.pro.b04.android.Question

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import ch.heigvd.pro.b04.android.Datamodel.Answer
import ch.heigvd.pro.b04.android.Datamodel.Question
import ch.heigvd.pro.b04.android.Network.*
import ch.heigvd.pro.b04.android.Network.RockinAPI.Companion.voteForAnswerSuspending
import ch.heigvd.pro.b04.android.Network.RequestsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class QuestionViewModel(application: Application, question : Question, private val token : String)
        : RequestsViewModel(application, question.idModerator.toInt(), question.idPoll.toInt(), token) {

    private val previousQuestion : Flow<Question?>
    private val nextQuestion : Flow<Question?>
    private val nbCheckedAnswer : MutableLiveData<Int> = MutableLiveData(0)

    val currentQuestion : MutableLiveData<Question> = MutableLiveData(question)

    val answers: Flow<List<Answer>>
    private val questionVMErrors : Flow<NetworkError>

    init {
        val answersDelayed : Flow<Response<List<Answer>>> = flow {
            while(true) {
                try {
                    currentQuestion.asFlow().collectLatest {
                        emit(RockinAPI.getAnswersSuspending(question, token))
                    }
                } catch (any : Exception) {}
                delay(DELAY)
            }
        }.broadcastIn(viewModelScope).asFlow()

        val answersUpdate : Flow<Response<List<Answer>>> = currentQuestion
            .asFlow()
            .map { RockinAPI.getAnswersSuspending(it, token) }
            .catch {}
            .filterNotNull()

        val requestAnswers = flowOf(
            answersDelayed.onEach { delay(7500) },
            answersUpdate
        ).flattenMerge()
        
        answers = requestAnswers.keepBody()

        val answerErrors = requestAnswers.keepError()

        questionVMErrors = flowOf(answerErrors, requestsVMErrors).flattenMerge<NetworkError>()

        val questionRelative : Flow<Pair<Question, List<Question>>> = currentQuestion
            .asFlow()
            .zip(questions) { x, y -> x to y }

        previousQuestion = questionRelative.map {(current, all) ->
            var candidate : Question? = null
            var candidateIndex = Double.MIN_VALUE

            all.forEach {
                val newIndex = it.indexInPoll
                if (newIndex < current.indexInPoll && newIndex > candidateIndex) {
                    candidate = it
                    candidateIndex = newIndex
                }
            }

            return@map candidate
        }

        nextQuestion = questionRelative.map {(current, all) ->
            var candidate : Question? = null
            var candidateIndex = Double.MAX_VALUE

            all.forEach {
                val newIndex = it.indexInPoll
                if (newIndex > current.indexInPoll && newIndex < candidateIndex) {
                    candidate = it
                    candidateIndex = newIndex
                }
            }

            return@map candidate
        }
    }

    fun selectAnswer(answer: Answer) {
        val question: Question? = currentQuestion.value

        if (question == null || question.idQuestion != answer.idQuestion)
            return

        var counter: Int = nbCheckedAnswer.value!!

        if (question.answerMax > counter || question.answerMax == 0 || answer.isChecked) {

            if (answer.isChecked) {
                counter++
            } else {
                counter--
            }
            nbCheckedAnswer.value = counter

            answer.toggle()
            // Note that for now, we do not take the result into account
            viewModelScope.launch {
                voteForAnswerSuspending(answer, token)
            }
        }
    }

    fun changeToPreviousQuestion() : Unit {
        viewModelScope.launch {
            previousQuestion.collectLatest {
                if (it != null)
                    currentQuestion.value = it
            }
        }
    }

    fun changeToNextQuestion() : Unit {
        viewModelScope.launch {
            nextQuestion.collectLatest {
                if (it != null)
                    currentQuestion.value = it
            }
        }
    }

    fun getNbCheckedAnswer() : LiveData<Int> {
        return nbCheckedAnswer
    }
}