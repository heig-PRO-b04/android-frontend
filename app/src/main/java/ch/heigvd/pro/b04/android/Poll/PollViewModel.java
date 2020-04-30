package ch.heigvd.pro.b04.android.Poll;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Poll.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Datamodel.PollDataModel;
import ch.heigvd.pro.b04.android.Network.RetrofitClient;
import ch.heigvd.pro.b04.android.Network.RockinAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollViewModel extends ViewModel {
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
    private Callback<PollDataModel> callbackPoll = new Callback<PollDataModel>() {
        @Override
        public void onResponse(Call<PollDataModel> call, Response<PollDataModel> response) {
            if (response.isSuccessful()) {
                Log.w("localDebug", call.request().url().toString());
                poll.postValue(new Poll(response.body().getIdPoll(), response.body().getIdModerator(), response.body().getTitle()));

                RetrofitClient.getRetrofitInstance()
                        .create(RockinAPI.class)
                        .getQuestions(response.body().getIdModerator(), response.body().getIdPoll(), call.request().url().query().substring(6))
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
        public void onFailure(Call<PollDataModel> call, Throwable t) {
            Log.e("localDebug", "We had a super bad error in callbackPollDataModel");
        }
    };

    /**
     * Constructor
     */
    public PollViewModel() {}

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
        RetrofitClient.getRetrofitInstance()
                .create(RockinAPI.class)
                .getPoll(idModerator, idPoll, token)
                .enqueue(callbackPoll);
    }
}
