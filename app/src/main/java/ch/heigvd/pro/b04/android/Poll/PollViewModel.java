package ch.heigvd.pro.b04.android.Poll;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Question.Question;


public class PollViewModel extends ViewModel {
    private MutableLiveData<List<Question>> questions = new MutableLiveData<>(new LinkedList<>());

    public PollViewModel() {}

    public void addQuestion(Question question) {
        this.questions.getValue().add(question);
   }

    public void goToQuestion(Question question) {
        // TODO : go to the question view
        System.out.println("Question was selected...\n");
    }

    public MutableLiveData<List<Question>> getQuestions() {
        return questions;
    }
}
