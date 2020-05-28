package ch.heigvd.pro.b04.android.authentication;

import android.util.Pair;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.function.Function;

import ch.heigvd.pro.b04.android.datamodel.Session;
import ch.heigvd.pro.b04.android.network.LiveDataUtils;
import ch.heigvd.pro.b04.android.network.Rockin;

/**
 * Maps a certain token to the associated {@link Session}.
 */
public class TokenToTokenWithSession implements Function<String, LiveData<Pair<String, Session>>> {

    @Override
    public LiveData<Pair<String, Session>> apply(String token) {
        LiveData<Session> sessions = LiveDataUtils.ignorePendingAndErrors(Rockin.api().getSession(token));
        return Transformations.map(sessions, s -> Pair.create(token, s));
    }
}
