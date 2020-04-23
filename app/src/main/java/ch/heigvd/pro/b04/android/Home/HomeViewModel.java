package ch.heigvd.pro.b04.android.Home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ch.heigvd.pro.b04.android.datamodel.SessionCode;
import ch.heigvd.pro.b04.android.datamodel.Token;
import ch.heigvd.pro.b04.android.network.RetrofitClient;
import ch.heigvd.pro.b04.android.network.RockinAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class HomeViewModel extends ViewModel {
    private MutableLiveData<List<Emoji>> queue = new MutableLiveData<>();

    private MutableLiveData<Set<Emoji>> selectedEmoji = new MutableLiveData<>();

    private MutableLiveData<String> registrationCode = new MutableLiveData<>();
    private MutableLiveData<List<Emoji>> registrationCodeEmoji = new MutableLiveData<>();

    private MutableLiveData<Token> token = new MutableLiveData<>();

    private Callback<Token> callbackToken = new Callback<Token>() {
        @Override
        public void onResponse(Call<Token> call, Response<Token> response) {
            if (response.isSuccessful()) {
                token.postValue(response.body());
            } else {
                Token error = new Token("Error");
                token.postValue(error);
                Log.w("localDebug", "Received error, HTTP status is " + response.code());
                Log.w("localDebug", "Registration code was : " + registrationCode.getValue());
                try {
                    Log.w("localDebug", response.errorBody().string());
                } catch (IOException e) {
                    Log.e("localDebug", "Error in error, rip");
                }
            }
        }

        @Override
        public void onFailure(Call<Token> call, Throwable t) {
            Log.e("localDebug", "We had a super bad error in callbackToken");
        }
    };

    public HomeViewModel() {
    }

    public void addNewEmoji(Emoji emoji) {

        // TODO : Factorize this logic in a model class, and test it.

        List<Emoji> buffer = queue.getValue();
        List<Emoji> emojisBuffer = registrationCodeEmoji.getValue();

        if (buffer == null) buffer = new LinkedList<>();
        if (emojisBuffer == null) emojisBuffer = new LinkedList<>();

        buffer.add(emoji);
        emojisBuffer.add(emoji);

        if (buffer.size() == 4) {
            Iterator<Emoji> emojis = buffer.iterator();
            StringBuilder code = new StringBuilder();
            code.append("0x");
            while (emojis.hasNext()) {
                code.append(emojis.next().getHex());
            }
            buffer.clear();
            emojisBuffer.clear();
            registrationCode.postValue(code.toString());

            RetrofitClient.getRetrofitInstance()
                    .create(RockinAPI.class)
                    .postConnect(new SessionCode(code.toString()))
                    .enqueue(callbackToken);
        }

        queue.postValue(buffer);
        registrationCodeEmoji.postValue(emojisBuffer);
        selectedEmoji.postValue(new HashSet<>(buffer));
    }

    public LiveData<List<Emoji>> getCodeEmoji() {
        return this.registrationCodeEmoji;
    }

    public LiveData<Set<Emoji>> getSelectedEmoji() {
        return this.selectedEmoji;
    }

    public LiveData<Token> getToken() {
        return this.token;
    }
}
