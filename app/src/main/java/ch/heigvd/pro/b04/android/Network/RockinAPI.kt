package ch.heigvd.pro.b04.android.Network

import androidx.lifecycle.LiveData
import ch.heigvd.pro.b04.android.Datamodel.*
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
    fun getQuestions(
            @Path("idModerator") idModerator: Long,
            @Path("idPoll") idPoll: Long,
            @Query("token") userToken: String?
    ): LiveData<ApiResponse<List<Question?>?>?>

    @GET("/mod/{idModerator}/poll/{idPoll}/question")
    suspend fun getQuestionsSuspending(
            @Path("idModerator") idModerator: Long,
            @Path("idPoll") idPoll: Long,
            @Query("token") userToken: String?
    ): Response<List<Question>>

    @GET("/mod/{idModerator}/poll/{idPoll}/question")
    fun getQuestionsViaCall(
            @Path("idModerator") idModerator: Long,
            @Path("idPoll") idPoll: Long,
            @Query("token") userToken: String?
    ): Call<List<Question?>?>

    @GET("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}")
    fun getQuestion(
            @Path("idModerator") idModerator: Long,
            @Path("idPoll") idPoll: Long,
            @Path("idQuestion") idQuestion: Long,
            @Query("token") token: String?
    ): Call<Question?>

    @GET("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer")
    fun getAnswers(
            @Path("idModerator") idModerator: Long,
            @Path("idPoll") idPoll: Long,
            @Path("idQuestion") idQuestion: Long,
            @Query("token") token: String?
    ): LiveData<ApiResponse<List<Answer?>?>?>

    @GET("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer")
    fun getAnswersViaCall(
            @Path("idModerator") idModerator: Long,
            @Path("idPoll") idPoll: Long,
            @Path("idQuestion") idQuestion: Long,
            @Query("token") token: String?
    ): Call<List<Answer?>?>

    @PUT("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}/vote")
    fun voteForAnswer(
            @Path("idModerator") idModerator: Long,
            @Path("idPoll") idPoll: Long,
            @Path("idQuestion") idQuestion: Long,
            @Path("idAnswer") idAnswer: Long,
            @Query("token") token: String?,
            @Body answer: Answer?
    ): Call<ResponseBody?>

    companion object {
        fun getQuestions(poll: Poll, userToken: String?): LiveData<ApiResponse<List<Question?>?>?> {
            return Rockin.api().getQuestions(
                    poll.idModerator.toLong(),
                    poll.idPoll.toLong(),
                    userToken
            )
        }

        suspend fun getQuestionsSuspending(poll: Poll, userToken: String) : Response<List<Question>>
        {
            return Rockin.api().getQuestionsSuspending(
                    poll.idModerator.toLong(),
                    poll.idPoll.toLong(),
                    userToken
            )
        }

        fun getAnswers(question: Question, token: String?): LiveData<ApiResponse<List<Answer?>?>?> {
            return Rockin.api().getAnswers(
                    question.idModerator,
                    question.idPoll,
                    question.idQuestion,
                    token
            )
        }

        fun voteForAnswer(answer: Answer, token: String?): Call<ResponseBody?> {
            return Rockin.api().voteForAnswer(
                    answer.idModerator,
                    answer.idPoll,
                    answer.idQuestion,
                    answer.idAnswer,
                    token,
                    answer)
        }
    }
}