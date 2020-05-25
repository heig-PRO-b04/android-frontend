package ch.heigvd.pro.b04.android.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/**
 * A utilities class with some basic utilities to work with {@link LiveData}, and combining them.
 */
public class LiveDataUtils {
    private LiveDataUtils() {
        // Nothing to see here.
    }

    /**
     * Transforms a {@link LiveData} of {@link ApiResponse} into a {@link LiveData} of the unwrapped
     * {@link T} type.
     *
     * @param liveData The {@link LiveData} to unwrap.
     * @param <T>      The type to retrieve.
     * @return An unwrapped {@link LiveData}.
     */
    public static <T> LiveData<T> ignorePendingAndErrors(LiveData<ApiResponse<T>> liveData) {
        return Transformations.switchMap(liveData, input -> input
                .map(MutableLiveData::new)
                .orElseGet(MutableLiveData::new)
        );
    }
}
