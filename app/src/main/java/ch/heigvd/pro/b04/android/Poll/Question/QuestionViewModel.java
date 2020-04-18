package ch.heigvd.pro.b04.android.Poll.Question;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Answer.Answer;

public class QuestionViewModel extends ViewModel {
    private String question;
    private MutableLiveData<List<Answer>> answers = new MutableLiveData<>(new LinkedList<>());
    private MutableLiveData<List<Answer>> selected = new MutableLiveData<>(new LinkedList<>());

    public QuestionViewModel() {}

    public void setQuestion(String question) {
        this.question = question;
    }

    public void addAnswer(Answer answer) {
        this.answers.getValue().add(answer);
    }

    public MutableLiveData<List<Answer>> getAnswers() {
        return answers;
    }

    public MutableLiveData<List<Answer>> getSelectedAnswers() {
        return selected;
    }

    public String getQuestion() {
        return question;
    }
}
