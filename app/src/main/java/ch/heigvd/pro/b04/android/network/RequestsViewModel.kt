package ch.heigvd.pro.b04.android.network

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ch.heigvd.pro.b04.android.datamodel.Poll
import ch.heigvd.pro.b04.android.datamodel.Question
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.Response

@FlowPreview
open class RequestsViewModel(application: Application, idModerator : Int, idPoll : Int, token : String)
    : AndroidViewModel(application) {

    val poll : Flow<Poll>
    val questions : Flow<List<Question>>

    private val networkErrors : Flow<NetworkError>

    init {
        val polls : Flow<Response<Poll>> = flow {
            while(true) {
                try {
                    emit(Rockin.api.getPollSuspending(idModerator.toLong(), idPoll.toLong(), token))
                } catch (any : Exception) {}
                delay(DELAY)
            }
        }.broadcastIn(viewModelScope).asFlow()

        poll = polls.keepBody()

        val requestQuestion : Flow<Response<List<Question>>> = poll
                .map { RockinAPI.getQuestionsSuspending(it, token) }
                .catch {}
                .filterNotNull()


        questions = requestQuestion.keepBody()

        val pollError : Flow<NetworkError> = polls.keepError()
        val questionError : Flow<NetworkError> = requestQuestion.keepError()

        networkErrors = flowOf(questionError, pollError).flattenMerge()
    }

    open fun networkErrors(): Flow<NetworkError> {
        return networkErrors
    }
}