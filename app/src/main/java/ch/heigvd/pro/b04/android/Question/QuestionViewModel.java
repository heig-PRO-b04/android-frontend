package ch.heigvd.pro.b04.android.Question;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Answer;
import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Network.Rockin;
import ch.heigvd.pro.b04.android.Utils.LocalDebug;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionViewModel extends ViewModel {
    private MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    private MutableLiveData<List<Answer>> currentQuestionAnswers =
            new MutableLiveData<>(new LinkedList<>());

    private MutableLiveData<List<Question>> questions = new MutableLiveData<>(new LinkedList<>());

    private Callback<List<Answer>> callbackAnswers = new Callback<List<Answer>>() {
        @Override
        public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
            if (response.isSuccessful()) {
                saveAnswers(response.body());
            } else {
                LocalDebug.logUnsuccessfulRequest(call, response);
            }
        }

        @Override
        public void onFailure(Call<List<Answer>> call, Throwable t) {
            LocalDebug.logFailedRequest(call, t);
        }
    };

    private void saveAnswers(List<Answer> answers) {
        this.currentQuestionAnswers.postValue(answers);
    }

    public QuestionViewModel() {}

    public MutableLiveData<List<Answer>> getCurrentQuestionAnswers() {
        return currentQuestionAnswers;
    }

    public void requestAnswers(String token, Question question) {
        Rockin.api()
                .getAnswers(
                        question.getIdModerator(),
                        question.getIdPoll(),
                        question.getIdQuestion(),
                        token)
                .enqueue(callbackAnswers);
    }

    public void getAllQuestionsFromBackend(Poll poll, String token) {
        QuestionUtils.sendGetQuestionRequest(poll, token);
    }

    public LiveData<Question> getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question question) {
        currentQuestion.postValue(question);
    }
}
