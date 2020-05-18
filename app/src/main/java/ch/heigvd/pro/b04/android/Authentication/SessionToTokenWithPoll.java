package ch.heigvd.pro.b04.android.Authentication;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.function.Function;

import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Session;
import ch.heigvd.pro.b04.android.Network.LiveDataUtils;
import ch.heigvd.pro.b04.android.Network.Rockin;

/**
 * Maps a certain {@link Session} into a {@link LiveData} for the associated {@link Poll}.
 */
public class SessionToTokenWithPoll implements Function<Pair<String, Session>, LiveData<Pair<String, Poll>>> {

    @Override
    public LiveData<Pair<String, Poll>> apply(Pair<String, Session> input) {
        String token = input.first;
        Session session = input.second;
        LiveData<Poll> polls = LiveDataUtils.ignorePendingAndErrors(Rockin.api().getPoll(
                Integer.parseInt(session.getIdModerator()),
                Integer.parseInt(session.getIdPoll()),
                token
        ));
        return Transformations.map(polls, p -> Pair.create(token, p));
    }
}
