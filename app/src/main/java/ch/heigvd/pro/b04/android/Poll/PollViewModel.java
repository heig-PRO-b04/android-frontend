package ch.heigvd.pro.b04.android.Poll;

import android.app.Application;

import androidx.annotation.NonNull;

public class PollViewModel extends SharedViewModel {
    public PollViewModel(@NonNull Application application, String token, int idModerator, int idPoll) {
        super(application, token, idModerator, idPoll);
    }
}
