package ch.heigvd.pro.b04.android.network;

import ch.heigvd.pro.b04.android.datamodel.Session;
import ch.heigvd.pro.b04.android.datamodel.SessionCode;
import ch.heigvd.pro.b04.android.datamodel.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RockinAPI {

    @POST("/connect")
    Call<Token> postConnect(@Body SessionCode code);

    @GET("/session")
    Call<Session> getSession(@Body Token userToken);
}
