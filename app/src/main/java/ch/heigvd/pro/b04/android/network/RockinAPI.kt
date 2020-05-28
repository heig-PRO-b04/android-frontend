package ch.heigvd.pro.b04.android.network

import androidx.lifecycle.LiveData
import ch.heigvd.pro.b04.android.datamodel.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RockinAPI {
    @POST("/connect")
    fun postConnect(
            @Body code: SessionCode?
    ): Call<Token?>

    @GET("/session")
    fun getSession(
            @Query("token") userToken: String?
    ): LiveData<ApiResponse<Session?>?>

    @GET("/mod/{idModerator}/poll/{idPoll}")
    fun getPoll(
            @Path("idModerator") idModerator: Long,
            @Path("idPoll") idPoll: Long,
            @Query("token") userToken: String?
    ): LiveData<ApiResponse<Poll?>?>

    @GET("/mod/{idModerator}/poll/{idPoll}")
    suspend fun getPollSuspending(
            @Path("idModerator") idModerator: Long,
            @Path("idPoll") idPoll: Long,
            @Query("token") userToken: String?
    ): Response<Poll>

    @GET("/mod/{idModerator}/poll/{idPoll}/question")
    suspend fun getQuestionsSuspending(
            @Path("idModerator") idModerator: Long,
            @Path("idPoll") idPoll: Long,
            @Query("token") userToken: String?
    ): Response<List<Question>>

    @GET("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer")
    suspend fun getAnswersSuspending(
            @Path("idModerator") idModerator: Long,
            @Path("idPoll") idPoll: Long,
            @Path("idQuestion") idQuestion: Long,
            @Query("token") token: String?
    ): Response<List<Answer>>

    @PUT("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}/vote")
    suspend fun voteForAnswerSuspending(
        @Path("idModerator") idModerator: Long,
        @Path("idPoll") idPoll: Long,
        @Path("idQuestion") idQuestion: Long,
        @Path("idAnswer") idAnswer: Long,
        @Query("token") token: String?,
        @Body answer: Answer?
    ): ResponseBody

    /* Those methods are simply wrappers to give objects rather than a lot of primitive types */
    companion object {
        suspend fun getQuestionsSuspending(poll: Poll, userToken: String) : Response<List<Question>> {
            return Rockin.api.getQuestionsSuspending(
                    poll.idModerator.toLong(),
                    poll.idPoll.toLong(),
                    userToken
            )
        }

        suspend fun getAnswersSuspending(question: Question, token: String): Response<List<Answer>> {
            return Rockin.api.getAnswersSuspending(
                    question.idModerator,
                    question.idPoll,
                    question.idQuestion,
                    token
            )
        }

        suspend fun voteForAnswerSuspending(answer: Answer, token: String?): ResponseBody {
            return Rockin.api.voteForAnswerSuspending(
                answer.idModerator,
                answer.idPoll,
                answer.idQuestion,
                answer.idAnswer,
                token,
                answer)
        }
    }
}