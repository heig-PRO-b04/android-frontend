package ch.heigvd.pro.b04.android.home;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import ch.heigvd.pro.b04.android.authentication.AuthenticationTokenLiveData;
import ch.heigvd.pro.b04.android.authentication.SessionToTokenWithPoll;
import ch.heigvd.pro.b04.android.authentication.TokenToTokenWithSession;
import ch.heigvd.pro.b04.android.datamodel.Poll;

/**
 * A {@link androidx.lifecycle.ViewModel} in charge of providing access to whatever poll we are
 * currently logged in, and offering the poll information whenever it's needed.
 */
public class NavigateToPollViewModel extends AndroidViewModel {

    private LiveData<Pair<String, Poll>> displayedPoll;

    public NavigateToPollViewModel(@NonNull Application application) {
        super(application);

        displayedPoll = Transformations.switchMap(
                new AuthenticationTokenLiveData(application),
                maybeToken -> maybeToken.map(
                        token -> Transformations.switchMap(
                                new TokenToTokenWithSession().apply(token),
                                values -> new SessionToTokenWithPoll().apply(values)
                        ))
                        .orElseGet(MutableLiveData::new)
        );
    }

    LiveData<Pair<String, Poll>> displayedPoll() {
        return displayedPoll;
    }
}
