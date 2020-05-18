package ch.heigvd.pro.b04.android.Network;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<ApiResponse<T>>> {

    private final Type type;

    public LiveDataCallAdapter(Type type) {
        this.type = type;
    }

    @NotNull
    @Override
    public LiveData<ApiResponse<T>> adapt(@NotNull Call<T> call) {
        return new LiveData<ApiResponse<T>>(ApiResponse.pending()) {

            private boolean success;

            @Override
            protected void onActive() {
                super.onActive();
                if (!success) enqueue();
            }

            @Override
            protected void onInactive() {
                super.onInactive();
                if (!call.isExecuted()) call.cancel();
            }

            private void enqueue() {
                call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(Call<T> call, Response<T> response) {
                        if (response.isSuccessful()) {
                            postValue(ApiResponse.of(response.body()));
                        } else {
                            postValue(ApiResponse.ofError(response.code()));
                        }
                        success = true;
                    }

                    @Override
                    public void onFailure(Call<T> call, Throwable t) {
                        postValue(ApiResponse.ofError(500));
                        success = false;
                    }
                });
            }
        };
    }

    @NotNull
    @Override
    public Type responseType() {
        return this.type;
    }
}
