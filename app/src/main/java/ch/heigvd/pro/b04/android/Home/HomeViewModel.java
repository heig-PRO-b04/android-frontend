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

import ch.heigvd.pro.b04.android.datamodel.Session;
import ch.heigvd.pro.b04.android.datamodel.SessionCode;
import ch.heigvd.pro.b04.android.datamodel.Token;
import ch.heigvd.pro.b04.android.network.RetrofitClient;
import ch.heigvd.pro.b04.android.network.RockinAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class HomeViewModel extends ViewModel {
    private String token;
    private MutableLiveData<List<String>> pollInfo = new MutableLiveData<>();

    private MutableLiveData<List<Emoji>> queue = new MutableLiveData<>();

    private MutableLiveData<Set<Emoji>> selectedEmoji = new MutableLiveData<>();

    private MutableLiveData<String> registrationCode = new MutableLiveData<>();

    private Callback<Session> callbackSession = new Callback<Session>() {
        @Override
        public void onResponse(Call<Session> call, Response<Session> response) {
            if (response.isSuccessful()) {
                Log.w("localDebug", "Success, session is " + response.body().getStatus());
                List<String> info = new LinkedList<>();
                info.add(response.body().getIdPoll());
                info.add(response.body().getIdModerator());
                pollInfo.postValue(info);
            } else {
                Log.w("localDebug", "Received error, HTTP status is " + response.code());
                Log.w("localDebug", "The request was " + call.request().url());

                try {
                    Log.w("localDebug", response.errorBody().string());
                } catch (IOException e) {
                    Log.e("localDebug", "Error in error, rip");
                }
            }
        }

        @Override
        public void onFailure(Call<Session> call, Throwable t) {
            Log.e("localDebug", "We had a super bad error in callbackToken");
        }
    };
    private MutableLiveData<List<Emoji>> registrationCodeEmoji = new MutableLiveData<>();

    private Callback<Token> callbackToken = new Callback<Token>() {
        @Override
        public void onResponse(Call<Token> call, Response<Token> response) {
            if (response.isSuccessful()) {
                token = response.body().getToken();
                RetrofitClient.getRetrofitInstance()
                        .create(RockinAPI.class)
                        .getSession(response.body().getToken())
                        .enqueue(callbackSession);
            } else {
                token = "Error";
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

        List<Emoji> emojisBuffer = registrationCodeEmoji.getValue();

        if (emojisBuffer == null) emojisBuffer = new LinkedList<>();

        emojisBuffer.add(emoji);

        if (emojisBuffer.size() == 4) {
            Iterator<Emoji> emojis = emojisBuffer.iterator();
            StringBuilder code = new StringBuilder();
            code.append("0x");
            while (emojis.hasNext()) {
                code.append(emojis.next().getHex());
            }
            emojisBuffer.clear();
            registrationCode.postValue(code.toString());

            RetrofitClient.getRetrofitInstance()
                    .create(RockinAPI.class)
                    .postConnect(new SessionCode(code.toString()))
                    .enqueue(callbackToken);

        }

        queue.postValue(emojisBuffer);
        registrationCodeEmoji.postValue(emojisBuffer);
        selectedEmoji.postValue(new HashSet<>(emojisBuffer));
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

    public String getToken() {
        return token;
    }

}
