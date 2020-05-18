package ch.heigvd.pro.b04.android.Authentication;

import android.util.Pair;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.function.Function;

import ch.heigvd.pro.b04.android.Datamodel.Session;
import ch.heigvd.pro.b04.android.Network.LiveDataUtils;
import ch.heigvd.pro.b04.android.Network.Rockin;

public class TokenToTokenWithSession implements Function<String, LiveData<Pair<String, Session>>> {

    @Override
    public LiveData<Pair<String, Session>> apply(String token) {
        LiveData<Session> sessions = LiveDataUtils.ignorePendingAndErrors(Rockin.api().getSession(token));
        return Transformations.map(sessions, s -> Pair.create(token, s));
    }
}
