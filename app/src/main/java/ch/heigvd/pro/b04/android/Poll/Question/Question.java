package ch.heigvd.pro.b04.android.Poll.Question;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Answer.Answer;

public class Question {
    private int id;
    private String question;
    private List<Answer> answers;
    private List<Answer> selectedAnswers;

    public Question(int id, String question, List<Answer> answers){
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.selectedAnswers = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public void answerWith(Answer answer) {
        if (!selectedAnswers.contains(answer) && answers.contains(answer))
            selectedAnswers.add(answer);
    }

    public List<Answer> getSelectedAnswers() {
        return selectedAnswers;
    }

    @NonNull
    @Override
    public String toString() {
        return "Question n° " + id + " : " + question;
    }
}
