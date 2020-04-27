package ch.heigvd.pro.b04.android.network;

import java.util.List;

import ch.heigvd.pro.b04.android.datamodel.PollDataModel;
import ch.heigvd.pro.b04.android.datamodel.QuestionDataModel;
import ch.heigvd.pro.b04.android.datamodel.Session;
import ch.heigvd.pro.b04.android.datamodel.SessionCode;
import ch.heigvd.pro.b04.android.datamodel.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RockinAPI {

    @POST("/connect")
    Call<Token> postConnect(@Body SessionCode code);

    @GET("/session")
    Call<Session> getSession(@Query("token") String userToken);

    @GET("/mod/{idModerator}/poll/{idPoll}")
    Call<PollDataModel> getPoll(@Path("idModerator") String idModerator, @Path("idPoll") String idPoll, @Query("token") String userToken);

    @GET("/mod/{idModerator}/poll/{idPoll}/question")
    Call<List<QuestionDataModel>> getQuestions(@Path("idModerator") String idModerator, @Path("idPoll") String idPoll, @Query("token") String userToken);
}
