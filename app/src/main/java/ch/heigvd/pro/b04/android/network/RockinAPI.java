package ch.heigvd.pro.b04.android.network;

import ch.heigvd.pro.b04.android.datamodel.Poll;
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

    @GET("/mod/{mod}/poll/{poll}")
    Call<Poll> getPoll(@Path("mod") String idModerator,@Path("poll") String idPoll, @Query("token") String userToken);
}
