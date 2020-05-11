package ch.heigvd.pro.b04.android.Network;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {

    private final Type type;

    public LiveDataCallAdapter(Type type) {
        this.type = type;
    }

    @NotNull
    @Override
    public LiveData<T> adapt(@NotNull Call<T> call) {
        return new LiveData<T>() {

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
                        postValue(response.body());
                        success = true;
                    }

                    @Override
                    public void onFailure(Call<T> call, Throwable t) {
                        success = false;
                        // TODO : Handle this case.
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
