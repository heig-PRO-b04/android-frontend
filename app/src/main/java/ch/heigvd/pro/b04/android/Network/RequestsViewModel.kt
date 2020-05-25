package ch.heigvd.pro.b04.android.Network

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ch.heigvd.pro.b04.android.Datamodel.Poll
import ch.heigvd.pro.b04.android.Datamodel.Question
import ch.heigvd.pro.b04.android.Network.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
open class RequestsViewModel(application: Application, idModerator : Int, idPoll : Int, token : String)
    : AndroidViewModel(application) {

    val poll : Flow<Poll>
    val questions : Flow<List<Question>>
    val requestsVMErrors : Flow<NetworkError>

    init {
        val polls : Flow<Response<Poll>> = flow {
            while(true) {
                try {
                    emit(Rockin.api().getPollSuspending(idModerator.toLong(), idPoll.toLong(), token))
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

        requestsVMErrors = flowOf(questionError, pollError).flattenMerge()
    }
}