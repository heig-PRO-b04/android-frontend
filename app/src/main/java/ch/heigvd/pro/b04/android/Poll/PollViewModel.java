package ch.heigvd.pro.b04.android.Poll;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Question.Question;


public class PollViewModel extends ViewModel {
    private List<Question> questions = new LinkedList<>();
    private MutableLiveData<List<Question>> queue = new MutableLiveData<>();

    public PollViewModel() {}

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void goToQuestion(Question question) {
        // TODO : go to the question view
        System.out.println("Question was selected...\n");
    }
}
