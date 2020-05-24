package ch.heigvd.pro.b04.android.Utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ch.heigvd.pro.b04.android.Datamodel.Poll
import ch.heigvd.pro.b04.android.Datamodel.Question
import ch.heigvd.pro.b04.android.Network.Rockin
import ch.heigvd.pro.b04.android.Network.RockinAPI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.Response

enum class ResponseError {
    TokenNotValid,
    NotFound
}

private const val DELAY : Long = 1000

private fun <T> errorFrom(response: Response<T>): ResponseError? {
    return when (response.code()) {
        404 -> ResponseError.NotFound
        else -> null
    }
}

fun <T> Flow<Response<T>>.keepError() : Flow<ResponseError> =
        filter { it.isSuccessful.not() }
                .map { errorFrom(it) }
                .filterNotNull()

fun <T:Any> Flow<Response<T>>.keepBody() : Flow<T> =
        filter { it.isSuccessful }
                .map { it.body() }
                .filterNotNull()

open class RequestsViewModel(application: Application, idModerator : Int, idPoll : Int, token : String)
    : AndroidViewModel(application) {

    val poll : Flow<Poll>
    val questions : Flow<List<Question>>
    val responseError : Flow<ResponseError>

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

        val pollError : Flow<ResponseError> = polls.keepError()
        val questionError : Flow<ResponseError> = requestQuestion.keepError()

        responseError = flowOf(questionError, pollError).flattenMerge()
    }
}