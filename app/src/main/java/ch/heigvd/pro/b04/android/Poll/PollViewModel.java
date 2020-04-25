package ch.heigvd.pro.b04.android.Poll;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Poll.Poll;
import ch.heigvd.pro.b04.android.Poll.Question.Question;
import ch.heigvd.pro.b04.android.datamodel.Session;
import ch.heigvd.pro.b04.android.datamodel.Token;
import ch.heigvd.pro.b04.android.network.RetrofitClient;
import ch.heigvd.pro.b04.android.network.RockinAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollViewModel extends ViewModel {
    private MutableLiveData<Poll> poll = new MutableLiveData<>();
    private MutableLiveData<Question> questionToView = new MutableLiveData<>();
    private MutableLiveData<List<Question>> questions = new MutableLiveData<>(new LinkedList<>());

    private Callback<Session> callbackSession = new Callback<Session>() {
        @Override
        public void onResponse(Call<Session> call, Response<Session> response) {
            if (response.isSuccessful()) {
                Log.w("localDebug", "Success, session is " + response.body().getStatus());
                poll.postValue(new Poll(response.body().getIdPoll(), response.body().getIdModerator(), response.body().getStatus()));
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

    public void getSession(Token token) {
        RetrofitClient.getRetrofitInstance()
                .create(RockinAPI.class)
                .getSession(token.getToken())
                .enqueue(callbackSession);
    }
}
