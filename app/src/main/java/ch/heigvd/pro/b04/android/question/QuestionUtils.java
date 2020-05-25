package ch.heigvd.pro.b04.android.question;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.datamodel.Poll;
import ch.heigvd.pro.b04.android.datamodel.Question;
import ch.heigvd.pro.b04.android.network.Rockin;
import ch.heigvd.pro.b04.android.utils.LocalDebug;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionUtils {
    private static QuestionUtils instance;
    private static int DELAY_IN_MILLIS = 1000;

    private MutableLiveData<List<Question>> localQuestions = new MutableLiveData<>(new LinkedList<>());
    private Poll poll;
    private String token;

    private QuestionUtils(Poll poll, String token) {
        setup(poll, token);
    }

    private QuestionUtils() {
    }

    private void setup(Poll poll, String token) {
        this.poll = poll;
        this.token = token;

        setupRepeatedHandler();
    }

    private void setupRepeatedHandler() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable(){
            public void run(){
                Rockin.api()
                        .getQuestionsViaCall(poll.getIdModerator(), poll.getIdPoll(), token)
                        .enqueue(callbackQuestions);
                handler.postDelayed(this, DELAY_IN_MILLIS);
            }
        }, DELAY_IN_MILLIS);
    }

    private Callback<List<Question>> callbackQuestions = new Callback<List<Question>>() {
        @Override
        public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
            if (response.isSuccessful()) {
                localQuestions.postValue(response.body());
            } else {
                LocalDebug.logUnsuccessfulRequest(call, response);
            }
        }

        @Override
        public void onFailure(Call<List<Question>> call, Throwable t) {
            LocalDebug.logFailedRequest(call, t);
        }
    };

    public static void sendGetQuestionRequest(Poll poll, String token) {
        if (instance == null) {
            instance = new QuestionUtils(poll, token);
        } else {
            instance.setup(poll, token);
        }
    }

    public static MutableLiveData<List<Question>> getQuestions() {
        if (instance == null)
            instance = new QuestionUtils();

        return instance.localQuestions;
    }
}
