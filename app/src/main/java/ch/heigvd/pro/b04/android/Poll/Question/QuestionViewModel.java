package ch.heigvd.pro.b04.android.Poll.Question;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Answer.Answer;

public class QuestionViewModel extends ViewModel {
    private MutableLiveData<List<Answer>> question = new MutableLiveData<>(new LinkedList<>());
    private MutableLiveData<List<Answer>> answers = new MutableLiveData<>(new LinkedList<>());

    public QuestionViewModel() {}

    public MutableLiveData<List<Answer>> getAnswers() {
        return answers;
    }

    public void getQuestions(int idModerator, int idPoll, int idQuestion) {
        //TODO : get question from backend
    }

    public Question getQuestion(int idModerator, int idPoll, int idQuestion) {
        //TODO : get question from backend
        return  new Question(0,0,0,"", "", 0, 0);
    }
}
