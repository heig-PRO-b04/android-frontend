package ch.heigvd.pro.b04.android.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
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

import ch.heigvd.pro.b04.android.authentication.AuthenticationTokenLiveData;
import ch.heigvd.pro.b04.android.datamodel.SessionCode;
import ch.heigvd.pro.b04.android.datamodel.Token;
import ch.heigvd.pro.b04.android.network.Rockin;
import ch.heigvd.pro.b04.android.utils.LocalDebug;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ch.heigvd.pro.b04.android.home.State.ERROR;
import static ch.heigvd.pro.b04.android.home.State.NORMAL;
import static ch.heigvd.pro.b04.android.home.State.SENDING;

public final class HomeViewModel extends AndroidViewModel {
    private MutableLiveData<State> requestState = new MutableLiveData<>(NORMAL);
    private MutableLiveData<Set<Emoji>> selectedEmoji = new MutableLiveData<>(new HashSet<>());
    private MutableLiveData<String> registrationCode = new MutableLiveData<>();
    private MutableLiveData<List<Emoji>> registrationCodeEmoji = new MutableLiveData<>(new ArrayList<>());
    private AuthenticationTokenLiveData tokenData = new AuthenticationTokenLiveData(getApplication());

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    private Callback<Token> callbackToken = new Callback<Token>() {
        @Override
        public void onResponse(Call<Token> call, Response<Token> response) {
            if (response.isSuccessful()) {
                onSuccessfulToken(response.body().getToken());
            } else {
                requestState.postValue(ERROR);
                LocalDebug.logUnsuccessfulRequest(call, response);
                Log.w("localDebug", "Registration code was : " + registrationCode.getValue());
            }
        }

        @Override
        public void onFailure(Call<Token> call, Throwable t) {
            requestState.postValue(ERROR);
            LocalDebug.logFailedRequest(call, t);
        }
    };

    /**
     * Helper method to setup the current state when we successfully retrieve a token
     * @param token The retrieved token
     */
    private void onSuccessfulToken(String token) {
        Objects.requireNonNull(token);

        registrationCodeEmoji.postValue(new ArrayList<>());
        tokenData.login(token);
    }

    public void addNewEmoji(Emoji emoji) {
        if (requestState.getValue().equals(ERROR)) {
            reinitializeEmojiBuffer();
        }

        List<Emoji> emojisBuffer = registrationCodeEmoji.getValue();

        if (emojisBuffer == null) {
            emojisBuffer = new LinkedList<>();
        }

        if(emojisBuffer.size() < 4) {
            emojisBuffer.add(emoji);
        }

        if (emojisBuffer.size() == 4) {
            String code = buildCodeFromEmojis();
            sendConnectRequest(code);
        }

        saveEmojiBufferState(emojisBuffer);
    }

    /**
     * Helper method used to clean up the buffer state
     */
    public void reinitializeEmojiBuffer() {
        requestState.setValue(NORMAL);
        registrationCodeEmoji.setValue(new ArrayList<>());
    }

    /**
     * Helper method that build the code as required by the API based on the emojis entered
     * @return A correctly formatted code
     */
    private String buildCodeFromEmojis() {
        Iterator<Emoji> emojis = registrationCodeEmoji.getValue().iterator();
        StringBuilder code = new StringBuilder().append("0x");

        while (emojis.hasNext()) {
            code.append(emojis.next().getHex());
        }
        return code.toString();
    }

    /**
     * Helper method used to send a connection request to the server
     */
    public void sendConnectRequest(String code) {
        requestState.postValue(SENDING);
        registrationCode.postValue(code);
        Rockin.api().postConnect(new SessionCode(code)).enqueue(callbackToken);
    }

    /**
     * Helper method to save the state of the emoji buffer
     * @param emojisBuffer The list of Emoji that we want to save
     */
    private void saveEmojiBufferState(@NonNull List<Emoji> emojisBuffer) {
        registrationCodeEmoji.postValue(emojisBuffer);
        selectedEmoji.postValue(new HashSet<>(emojisBuffer));
    }

    public LiveData<List<Emoji>> getCodeEmoji() {
        return this.registrationCodeEmoji;
    }

    public void clearOneEmoji() {
        List<Emoji> emojiList = registrationCodeEmoji.getValue();
        if (! emojiList.isEmpty()) {
            emojiList.remove(emojiList.size() - 1);
            registrationCodeEmoji.postValue(emojiList);
            requestState.postValue(NORMAL);
        }
    }

    public LiveData<State> getRequestState() {
        return this.requestState;
    }
}
