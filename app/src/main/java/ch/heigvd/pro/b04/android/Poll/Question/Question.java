package ch.heigvd.pro.b04.android.Poll.Question;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Answer.Answer;

public class Question {
    private int id;
    private String question;
    private Answer[] answers;
    private List<Answer> selectedAnswers;

    public Question(int id, String question, Answer[] answers){
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.selectedAnswers = new LinkedList<>();
    }

    public void answer(Question question, Answer answer) {
        for (Answer a : question.answers ) {
            if(a == answer) {
                question.selectedAnswers.add(answer);
                break;
            }
        }
    }

    public String getQuestion() {
        return question;
    }

    public boolean answered() {
        return selectedAnswers.size() != 0;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public List<Answer> getSelectedAnswers() {
        return selectedAnswers;
    }

    public int getId() {
        return id;
    }
}
