package ch.heigvd.pro.b04.android.Utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * An implementation of a {@link ViewModelProvider.Factory} that supports the creation of some
 * {@link SharedViewModel}s with specific authentication tokens, and dedicated poll identifiers as
 * well.
 */
public class SharedViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private String token;
    private int idModerator;
    private int idPoll;

    public SharedViewModelFactory(
            @NonNull Application application,
            int idModerator,
            int idPoll,
            String token
    ) {
        this.application = application;
        this.idModerator = idModerator;
        this.idPoll = idPoll;
        this.token = token;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Constructor[] constructors = modelClass.getDeclaredConstructors();
        try {
            return (T) constructors[0].newInstance(application, idModerator, idPoll, token);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
