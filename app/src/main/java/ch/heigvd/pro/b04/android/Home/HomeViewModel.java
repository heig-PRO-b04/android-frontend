package ch.heigvd.pro.b04.android.Home;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ch.heigvd.pro.b04.android.Datamodel.Session;
import ch.heigvd.pro.b04.android.Datamodel.SessionCode;
import ch.heigvd.pro.b04.android.Datamodel.Token;
import ch.heigvd.pro.b04.android.Network.Rockin;
import ch.heigvd.pro.b04.android.R;
import ch.heigvd.pro.b04.android.Utils.LocalDebug;
import ch.heigvd.pro.b04.android.Utils.Persistent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class HomeViewModel extends AndroidViewModel {
    private Context context;
    private Boolean triedToGetToken = false;

    private MutableLiveData<List<String>> pollInfo = new MutableLiveData<>();
    private MutableLiveData<Integer> codeColor = new MutableLiveData<>();
    private MutableLiveData<List<Emoji>> queue = new MutableLiveData<>();
    private MutableLiveData<Set<Emoji>> selectedEmoji = new MutableLiveData<>();
    private MutableLiveData<String> registrationCode = new MutableLiveData<>();
    private MutableLiveData<List<Emoji>> registrationCodeEmoji = new MutableLiveData<>();

    /**
     * This helper method saves a session in our poll info Live Data
     * @param session The session to save
     */
    private void saveSessionInPollInfo(Session session) {
        Objects.requireNonNull(session);

        List<String> info = new LinkedList<>();
        info.add(session.getIdPoll());
        info.add(session.getIdModerator());
        pollInfo.postValue(info);
    }

    /**
     * Helper method that sets the current state correctly in case of error while retrieving the
     * token
     */
    private void setBadTokenErrorValues() {
        triedToGetToken = true;
        setEmojiCodeColor(R.color.colorError);
    }

    /**
     * Helper method to setup the current state when we successfully retrieve a token
     * @param token The retrieved token
     */
    private void onSuccessfulToken(String token) {
        Objects.requireNonNull(token);

        registrationCodeEmoji.postValue(new ArrayList<>());
        Persistent.writeToken(context, token);
        sendGetSessionRequest(token);
    }

    public void sendGetSessionRequest(String token) {
        Rockin.api().getSession(token).enqueue(callbackSession);
    }

    private Callback<Session> callbackSession = new Callback<Session>() {
        @Override
        public void onResponse(Call<Session> call, Response<Session> response) {
            if (response.isSuccessful()) {
                saveSessionInPollInfo(response.body());
            } else {
                LocalDebug.logUnsuccessfulRequest(call, response);
            }
        }

        @Override
        public void onFailure(Call<Session> call, Throwable t) {
            LocalDebug.logFailedRequest(call, t);
        }
    };

    private Callback<Token> callbackToken = new Callback<Token>() {
        @Override
        public void onResponse(Call<Token> call, Response<Token> response) {
            if (response.isSuccessful()) {
                onSuccessfulToken(response.body().getToken());
            } else {
                setBadTokenErrorValues();
                LocalDebug.logUnsuccessfulRequest(call, response);
                Log.w("localDebug", "Registration code was : " + registrationCode.getValue());
            }
        }

        @Override
        public void onFailure(Call<Token> call, Throwable t) {
            LocalDebug.logFailedRequest(call, t);
        }
    };

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = getApplication().getApplicationContext();
    }

    public void addNewEmoji(Emoji emoji) {
        List<Emoji> emojisBuffer = registrationCodeEmoji.getValue();

        if (emojisBuffer == null) {
            emojisBuffer = new LinkedList<>();
        }

        if (triedToGetToken) {
            reinitializeEmojiBuffer();
        }

        if(emojisBuffer.size() < 4) {
            emojisBuffer.add(emoji);
        }

        if (emojisBuffer.size() == 4) {
            sendConnectRequest();
        }

        saveEmojiBufferState(emojisBuffer);
    }

    /**
     * Helper method to save the state of the emoji buffer
     * @param emojisBuffer The list of Emoji that we want to save
     */
    private void saveEmojiBufferState(@NonNull List<Emoji> emojisBuffer) {
        queue.postValue(emojisBuffer);
        registrationCodeEmoji.postValue(emojisBuffer);
        selectedEmoji.postValue(new HashSet<>(emojisBuffer));
    }

    /**
     * Helper method used to send a connection request to the server
     */
    private void sendConnectRequest() {
        setEmojiCodeColor(R.color.seaside_050);
        Iterator<Emoji> emojis = registrationCodeEmoji.getValue().iterator();
        StringBuilder code = new StringBuilder().append("0x");

        while (emojis.hasNext()) {
            code.append(emojis.next().getHex());
        }

        registrationCode.postValue(code.toString());
        Rockin.api().postConnect(new SessionCode(code.toString())).enqueue(callbackToken);
    }

    /**
     * Helper method to change the color of the emoji code
     * @param color Represents the color, should be a resource like R.color.xxx or Color.xxx
     */
    private void setEmojiCodeColor(int color) {
        codeColor.postValue(ContextCompat.getColor(context, color));
    }

    /**
     * Helper method used to clean up the buffer state
     */
    private void reinitializeEmojiBuffer() {
        registrationCodeEmoji.getValue().clear();
        setEmojiCodeColor(R.color.seaside_200);
        triedToGetToken = false;
    }

    public LiveData<List<Emoji>> getCodeEmoji() {
        return this.registrationCodeEmoji;
    }

    public MutableLiveData<List<String>> getPollInfo() {
        return this.pollInfo;
    }

    public LiveData<Set<Emoji>> getSelectedEmoji() {
        return this.selectedEmoji;
    }

    public LiveData<Integer> getCodeColor() {
        return codeColor;
    }
}
