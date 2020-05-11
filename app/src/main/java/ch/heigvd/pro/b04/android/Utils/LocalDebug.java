package ch.heigvd.pro.b04.android.Utils;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LocalDebug {
    private static String TAG = "localDebug";
    private LocalDebug() {};

    public static <T> void logUnsuccessfulRequest(Call<T> call, Response<T> response) {
        Log.w(TAG, "Received error, HTTP status is " + response.code());
        Log.w(TAG, "The request was " + call.request().url());

        try {
            Log.w(TAG, response.errorBody().string());
        } catch (IOException e) {
            Log.e(TAG, "Parsing errorBody");
        }
    }

    public static <T> void logFailedRequest(Call<T> call, Throwable t) {
        Log.e(TAG, "Error in " + call.request().url() + " : " + t.getMessage());
    }
}
