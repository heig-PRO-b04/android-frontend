package ch.heigvd.pro.b04.android.Question;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Question.Answer.Answer;

public class QuestionViewModel extends ViewModel {
    private MutableLiveData<Question> question = new MutableLiveData<>();
    private MutableLiveData<List<Answer>> answers = new MutableLiveData<>(new LinkedList<>());

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
}
