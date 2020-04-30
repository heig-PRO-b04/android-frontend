package ch.heigvd.pro.b04.android.Poll;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Network.Rockin;
import ch.heigvd.pro.b04.android.Utils.Exceptions.TokenNotSetException;
import ch.heigvd.pro.b04.android.Utils.Persistent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Poll> poll = new MutableLiveData<>();
    private MutableLiveData<Question> questionToView = new MutableLiveData<>();
    private MutableLiveData<List<Question>> questions = new MutableLiveData<>(new LinkedList<>());

    /**********************
     * Callback variables *
     **********************/
    private Callback<List<Question>> callbackQuestions = new Callback<List<Question>>() {
        @Override
        public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
            if (response.isSuccessful()) {
                Log.w("localDebug", call.request().url().toString());
                questions.postValue(response.body());
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
        public void onFailure(Call<List<Question>> call, Throwable t) {
            Log.e("localDebug", "We had a super bad error in callbackQuestions : " + call.request().url());
            Log.e("localDebug", "The error is : " + t.getMessage());
        }
    };
    private Callback<Poll> callbackPoll = new Callback<Poll>() {
        @Override
        public void onResponse(Call<Poll> call, Response<Poll> response) {
            if (response.isSuccessful()) {
                Log.w("localDebug", call.request().url().toString());
                Poll resp = response.body();
                poll.postValue(resp);

                String token = null;
                try {
                    token = Persistent.getStoredTokenOrError(context);
                } catch (TokenNotSetException e) {
                    e.printStackTrace();
                }

                Rockin.api()
                        .getQuestions(resp.getIdModerator(), resp.getIdPoll(), token)
                        .enqueue(callbackQuestions);
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
        public void onFailure(Call<Poll> call, Throwable t) {
            Log.e("localDebug", "We had a super bad error in callbackPollDataModel");
        }
    };

    /**
     * Constructor
     */
    public PollViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<Poll> getPoll() {
        return poll;
    }

    public void goToQuestion(Question question) {
        System.out.println("Question was selected...\n");
        questionToView.postValue(question);
    }

    public MutableLiveData<List<Question>> getQuestions() {
        return questions;
    }

    public MutableLiveData<Question> getQuestionToView() {
        return questionToView;
    }

    public void getPoll(String idPoll, String idModerator, String token) {
        Rockin.api().getPoll(idModerator, idPoll, token).enqueue(callbackPoll);
    }
}
