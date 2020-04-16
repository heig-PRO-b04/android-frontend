package ch.heigvd.pro.b04.android.Poll;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ch.heigvd.pro.b04.android.Poll.Question.Question;


public class PollViewModel extends ViewModel {
    private MutableLiveData<List<Question>> queue = new MutableLiveData<>();

    private MutableLiveData<Set<Question>> answeredQuestion = new MutableLiveData<>();

    public PollViewModel() {}

    public List<Question> getQuestions() {
        return queue.getValue();
    }

    public LiveData<Set<Question>> getAnsweredQuestion() {
        return this.answeredQuestion;
    }

    public void goToQuestion(Question question) {
        // TODO : go to the question view
    }
}
