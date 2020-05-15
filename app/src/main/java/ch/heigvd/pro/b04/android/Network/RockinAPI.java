package ch.heigvd.pro.b04.android.Network;

import androidx.lifecycle.LiveData;

import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Answer;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Session;
import ch.heigvd.pro.b04.android.Datamodel.SessionCode;
import ch.heigvd.pro.b04.android.Datamodel.Token;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RockinAPI {

    @POST("/connect")
    Call<Token> postConnect(
            @Body SessionCode code
    );

    @GET("/session")
    LiveData<Session> getSession(
            @Query("token") String userToken
    );

    @GET("/mod/{idModerator}/poll/{idPoll}")
    LiveData<Poll> getPoll(
            @Path("idModerator") long idModerator,
            @Path("idPoll") long idPoll,
            @Query("token") String userToken
    );

    @GET("/mod/{idModerator}/poll/{idPoll}")
    Call<Poll> getPollViaCall(
            @Path("idModerator") long idModerator,
            @Path("idPoll") long idPoll,
            @Query("token") String userToken
    );

    @GET("/mod/{idModerator}/poll/{idPoll}/question")
    Call<List<Question>> getQuestions(
            @Path("idModerator") long idModerator,
            @Path("idPoll") long idPoll,
            @Query("token") String userToken
    );

    @GET("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}")
    Call<Question> getQuestion(
            @Path("idModerator") long idModerator,
            @Path("idPoll") long idPoll,
            @Path("idQuestion") long idQuestion,
            @Query("token") String token
    );

    @GET("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer")
    LiveData<List<Answer>> getAnswers(
            @Path("idModerator") long idModerator,
            @Path("idPoll") long idPoll,
            @Path("idQuestion") long idQuestion,
            @Query("token") String token
    );

    @GET("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer")
    Call<List<Answer>> getAnswersViaCall(
            @Path("idModerator") long idModerator,
            @Path("idPoll") long idPoll,
            @Path("idQuestion") long idQuestion,
            @Query("token") String token
    );

    @PUT("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}/vote")
    Call<ResponseBody> voteForAnswer(
            @Path("idModerator") long idModerator,
            @Path("idPoll") long idPoll,
            @Path("idQuestion") long idQuestion,
            @Path("idAnswer") long idAnswer,
            @Query("token") String token,
            @Body Answer answer
    );
}
