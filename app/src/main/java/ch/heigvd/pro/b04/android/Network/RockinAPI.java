package ch.heigvd.pro.b04.android.Network;

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
    Call<Session> getSession(
            @Query("token") String userToken
    );

    @GET("/mod/{idModerator}/poll/{idPoll}")
    Call<Poll> getPoll(
            @Path("idModerator") String idModerator,
            @Path("idPoll") String idPoll,
            @Query("token") String userToken
    );

    @GET("/mod/{idModerator}/poll/{idPoll}/question")
    Call<List<Question>> getQuestions(
            @Path("idModerator") String idModerator,
            @Path("idPoll") String idPoll,
            @Query("token") String userToken
    );

    @GET("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}")
    Call<Question> getQuestion(
            @Path("idModerator") String idModerator,
            @Path("idPoll") String idPoll,
            @Path("idQuestion") String idQuestion,
            @Query("token") String token
    );

    @GET("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer")
    Call<List<Answer>> getAnswers(
            @Path("idModerator") String idModerator,
            @Path("idPoll") String idPoll,
            @Path("idQuestion") String idQuestion,
            @Query("token") String token
    );

    @PUT("/mod/{idModerator}/poll/{idPoll}/question/{idQuestion}/answer/{idAnswer}/vote")
    Call<ResponseBody> voteForAnswer(
            @Path("idModerator") String idModerator,
            @Path("idPoll") String idPoll,
            @Path("idQuestion") String idQuestion,
            @Path("idAnswer") String idAnswer,
            @Query("token") String token
    );
}
