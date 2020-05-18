package ch.heigvd.pro.b04.android.Network;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * See https://gist.github.com/AkshayChordiya/15cfe7ca1842d6b959e77c04a073a98f for reference.
 */
public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Nullable
    @Override
    public CallAdapter<?, ?> get(
            @NotNull Type returnType,
            @NotNull Annotation[] annotations,
            @NotNull Retrofit retrofit
    ) {
        // We only adapt to LiveData return types.
        if (getRawType(returnType) != LiveData.class) {
            return null;
        }

        Type type = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = getRawType(type);

        if (rawObservableType != ApiResponse.class) {
            throw new IllegalArgumentException("Type must be an ApiResponse.");
        }

        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Response must be parameterized.");
        }

        return new LiveDataCallAdapter<>(getParameterUpperBound(0, (ParameterizedType) type));
    }
}
