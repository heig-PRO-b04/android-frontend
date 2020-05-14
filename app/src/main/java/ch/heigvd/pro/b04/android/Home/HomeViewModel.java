package ch.heigvd.pro.b04.android.Home;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
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

import ch.heigvd.pro.b04.android.Authentication.AuthenticationTokenLiveData;
import ch.heigvd.pro.b04.android.Datamodel.SessionCode;
import ch.heigvd.pro.b04.android.Datamodel.Token;
import ch.heigvd.pro.b04.android.Network.Rockin;
import ch.heigvd.pro.b04.android.R;
import ch.heigvd.pro.b04.android.Utils.LocalDebug;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class HomeViewModel extends AndroidViewModel {
    private Context context;
    private Boolean triedToGetToken = false;

    private MutableLiveData<Integer> codeColor = new MutableLiveData<>();
    private MutableLiveData<Drawable> clearButtonRes = new MutableLiveData<>();
    private MutableLiveData<Set<Emoji>> selectedEmoji = new MutableLiveData<>(new HashSet<>());
    private MutableLiveData<String> registrationCode = new MutableLiveData<>();
    private MutableLiveData<List<Emoji>> registrationCodeEmoji = new MutableLiveData<>(new ArrayList<>());
    private AuthenticationTokenLiveData tokenData = new AuthenticationTokenLiveData(getApplication());

    /**
     * Helper method that sets the current state correctly in case of error while retrieving the
     * token
     */
    private void setBadTokenErrorValues() {
        triedToGetToken = true;
        setEmojiCodeColor(R.color.colorError);
        setClearButtonToggle();
    }

    private void setClearButtonToggle() {
        Drawable resource = triedToGetToken
                ? ContextCompat.getDrawable(context, R.drawable.clear_emoji_error)
                : ContextCompat.getDrawable(context, R.drawable.clear_emoji);
        clearButtonRes.setValue(resource);
    }

    /**
     * Helper method to setup the current state when we successfully retrieve a token
     * @param token The retrieved token
     */
    private void onSuccessfulToken(String token) {
        Objects.requireNonNull(token);

        registrationCodeEmoji.postValue(new ArrayList<>());
        tokenData.login(token);
    }

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
        if (triedToGetToken) {
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
     * Helper method to save the state of the emoji buffer
     * @param emojisBuffer The list of Emoji that we want to save
     */
    private void saveEmojiBufferState(@NonNull List<Emoji> emojisBuffer) {
        registrationCodeEmoji.postValue(emojisBuffer);
        selectedEmoji.postValue(new HashSet<>(emojisBuffer));
    }

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
        setEmojiCodeColor(R.color.seaside_200);

        registrationCode.postValue(code);
        Rockin.api().postConnect(new SessionCode(code)).enqueue(callbackToken);
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
    public void reinitializeEmojiBuffer() {
        triedToGetToken = false;
        registrationCodeEmoji.setValue(new ArrayList<>());
        setEmojiCodeColor(android.R.color.white);
        setClearButtonToggle();
    }

    public LiveData<List<Emoji>> getCodeEmoji() {
        return this.registrationCodeEmoji;
    }

    public LiveData<Set<Emoji>> getSelectedEmoji() {
        return this.selectedEmoji;
    }

    public LiveData<Integer> getCodeColor() {
        return codeColor;
    }

    public void clearOneEmoji() {
        List<Emoji> emojiList = registrationCodeEmoji.getValue();
        if (! emojiList.isEmpty()) {
            emojiList.remove(emojiList.size() - 1);
            registrationCodeEmoji.postValue(emojiList);
        }
    }

    public LiveData<Drawable> getClearButtonRes() {
        return clearButtonRes;
    }
}
