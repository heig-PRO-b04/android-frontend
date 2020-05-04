package ch.heigvd.pro.b04.android.Utils;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LocalDebug {
    private LocalDebug() {};

    public static <T> void logUnsuccessfulRequest(Call<T> call, Response<T> response) {
        Log.w("localDebug", "Received error, HTTP status is " + response.code());
        Log.w("localDebug", "The request was " + call.request().url());

        try {
            Log.w("localDebug", response.errorBody().string());
        } catch (IOException e) {
            Log.e("localDebug", "Parsing errorBody");
        }
    }

    public static <T> void logFailedRequest(Call<T> call, Throwable t) {
        Log.e("localDebug", "Error in " + call.request().url() + " : " + t.getMessage());
    }
}
