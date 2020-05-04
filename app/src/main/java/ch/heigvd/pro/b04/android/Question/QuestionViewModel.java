package ch.heigvd.pro.b04.android.Question;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Answer;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Network.Rockin;
import ch.heigvd.pro.b04.android.Utils.LocalDebug;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionViewModel extends ViewModel {
    private MutableLiveData<Question> question = new MutableLiveData<>();
    private MutableLiveData<List<Answer>> answers = new MutableLiveData<>(new LinkedList<>());

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
        this.answers.postValue(answers);
    }

    public QuestionViewModel() {}

    public MutableLiveData<List<Answer>> getAnswers() {
        return answers;
    }

    public void setQuestion(Question q) {
        question.postValue(q);
    }

    public MutableLiveData<Question> getViewSelectedQuestion() {
        return question;
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

    public Question getPreviousQuestion() {
        //TODO List<Question> questions = Persistent.getQuestions();
        List<Question> questions = null;

        for (int i = 0; i < questions.size() - 1; i++) {
            if (questions.get(i + 1).getIdQuestion().equals(question.getValue().getIdQuestion())) {
                return questions.get(i);
            }
        }

        return questions.get(questions.size()-1);
    }

    public Question getNextQuestion() {
        // TODO List<Question> questions = Persistent.getQuestions();
        List<Question> questions = null;

        for (int i = 1; i < questions.size(); i++) {
            if (questions.get(i - 1).getIdQuestion().equals(question.getValue().getIdQuestion())) {
                return questions.get(i);
            }
        }

        return questions.get(0);
    }
}
