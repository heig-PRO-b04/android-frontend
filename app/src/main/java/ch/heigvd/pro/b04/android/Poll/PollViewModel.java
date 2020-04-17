package ch.heigvd.pro.b04.android.Poll;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Question.Question;


public class PollViewModel extends ViewModel {
    private MutableLiveData<List<Question>> queue = new MutableLiveData<>();

    public PollViewModel() {}

    public void addQuestion(Question question) {
        if (!queue.getValue().contains(question)) {
            List<Question> buffer = queue.getValue();

            if (buffer == null) buffer = new LinkedList<>();

            buffer.add(question);
            queue.postValue(buffer);
        }
    }

    public List<Question> getQuestions() {
        if(queue.getValue() != null)
            return queue.getValue();
        return new LinkedList<>();
    }

    public void goToQuestion(Question question) {
        // TODO : go to the question view
        System.out.println("Question was selected...\n");
    }
}
