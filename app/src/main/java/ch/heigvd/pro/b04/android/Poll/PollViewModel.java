package ch.heigvd.pro.b04.android.Poll;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Question.Question;

public class PollViewModel extends ViewModel {
    private MutableLiveData<List<Question>> questionToView = new MutableLiveData<>();
    private MutableLiveData<List<Question>> questions = new MutableLiveData<>(new LinkedList<>());

    public PollViewModel() {}

    public void addQuestion(Question question) {
        this.questions.getValue().add(question);
   }

    public void goToQuestion(Question question) {
        System.out.println("Question was selected...\n");
        List<Question> buffer = new LinkedList<>();
        buffer.add(question);
        questionToView.postValue(buffer);
    }

    public MutableLiveData<List<Question>> getQuestions() {
        return questions;
    }

    public MutableLiveData<List<Question>> getQuestionToView() {
        return questionToView;
    }
}
