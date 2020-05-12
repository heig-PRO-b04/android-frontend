package ch.heigvd.pro.b04.android.Poll;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ch.heigvd.pro.b04.android.Poll.PollViewModel;

/**
 * An implementation of a {@link ViewModelProvider.Factory} that supports the creation of some
 * {@link PollViewModel}s with specific authentication tokens, and dedicated poll identifiers as
 * well.
 */
public class PollViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    private String token;
    private int idModerator;
    private int idPoll;

    public PollViewModelFactory(
            @NonNull Application application,
            String token,
            int idModerator,
            int idPoll
    ) {
        this.application = application;
        this.token = token;
        this.idModerator = idModerator;
        this.idPoll = idPoll;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PollViewModel(application, token, idModerator, idPoll);
    }
}
