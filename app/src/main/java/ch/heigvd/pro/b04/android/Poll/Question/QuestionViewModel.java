package ch.heigvd.pro.b04.android.Poll.Question;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.QuestionDataModel;
import ch.heigvd.pro.b04.android.Poll.Answer.Answer;
import ch.heigvd.pro.b04.android.Network.RetrofitClient;
import ch.heigvd.pro.b04.android.Network.RockinAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionViewModel extends ViewModel {
    private MutableLiveData<Question> question = new MutableLiveData<>();
    private MutableLiveData<List<Answer>> answers = new MutableLiveData<>(new LinkedList<>());

    private Callback<QuestionDataModel> callbackQuestion = new Callback<QuestionDataModel>() {
        @Override
        public void onResponse(Call<QuestionDataModel> call, Response<QuestionDataModel> response) {
            if (response.isSuccessful()) {
                Log.w("localDebug", call.request().url().toString());
                Question respQuestion = new Question(response.body().getIdModerator(), response.body().getIdPoll(), response.body().getIdQuestion(),
                        response.body().getTitle(), response.body().getDetails(),
                        response.body().getAnswerMin(), response.body().getAnswerMax());
                question.postValue(respQuestion);

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
        public void onFailure(Call<QuestionDataModel> call, Throwable t) {
            Log.e("localDebug", "We had a super bad error in callbackQuestions : " + call.request().url());
            Log.e("localDebug", "The error is : " + t.getMessage());
        }
    };

    public QuestionViewModel() {}

    public MutableLiveData<List<Answer>> getAnswers() {
        return answers;
    }

    public void getQuestion(String idModerator, String idPoll, String idQuestion, String token) {
        RetrofitClient.getRetrofitInstance()
                .create(RockinAPI.class)
                .getQuestion(idModerator, idPoll, idQuestion, token)
                .enqueue(callbackQuestion);
    }

    public MutableLiveData<Question> getViewSelectedQuestion() {
        return question;
    }
}
