package ch.heigvd.pro.b04.android.network;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A wrapper class for API responses. This is very similar to the constructs that can be found in
 * the {@link Optional} class. Instances of this class are immutable, and can either be in a
 * success or in an error state.
 *
 * @param <T> The parameterized type of the response that is received.
 */
public final class ApiResponse<T> {
    private static final int DEFINED_CODE = -1;
    private static final int PENDING_CODE = 0;

    private T response;
    private int errorCode;

    public static <T> ApiResponse<T> of(T value) {
        return new ApiResponse<>(value);
    }

    public static <T> ApiResponse<T> pending() {
        return new ApiResponse<>(PENDING_CODE);
    }

    public static <T> ApiResponse<T> ofError(int code) {
        if (code <= PENDING_CODE) {
            throw new IllegalArgumentException("The response code must be greater than 0.");
        }
        return new ApiResponse<>(code);
    }

    private ApiResponse(int error) {
        response = null;
        errorCode = error;
    }

    private ApiResponse(T value) {
        response = Objects.requireNonNull(value);
        errorCode = DEFINED_CODE;
    }

    public boolean isSuccess() {
        return errorCode == DEFINED_CODE;
    }

    public boolean isPending() {
        return errorCode == PENDING_CODE;
    }

    public boolean isFailure() {
        return errorCode != DEFINED_CODE && errorCode != PENDING_CODE;
    }

    public Optional<Integer> error() {
        if (isFailure()) {
            return Optional.of(errorCode);
        } else {
            return Optional.empty();
        }
    }

    public Optional<T> response() {
        if (isFailure() || isPending()) {
            return Optional.empty();
        } else {
            return Optional.of(response);
        }
    }

    public <S> ApiResponse<S> map(Function<T, S> function) {
        if (isFailure() || isPending()) {
            return new ApiResponse<>(errorCode);
        } else {
            return new ApiResponse<>(function.apply(response));
        }
    }

    public <S> ApiResponse<S> flatMap(Function<T, ApiResponse<S>> function) {
        if (isFailure() || isPending()) {
            return new ApiResponse<>(errorCode);
        } else {
            return function.apply(response);
        }
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        if (isFailure() || isPending()) {
            return supplier.get();
        } else {
            return response;
        }
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> supplier) throws X {
        if (isFailure() || isPending()) {
            throw supplier.get();
        } else {
            return response;
        }
    }
}
