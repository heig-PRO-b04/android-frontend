package ch.heigvd.pro.b04.android.Poll.Question;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Answer.Answer;

public enum Question {

    Q0(0, "Is it sunny today?", new Answer[]{new Answer(0, "Yes"), new Answer(1, "No")}),
    Q1(1, "Is it rainy today?", new Answer[]{new Answer(2, "Yes"), new Answer(3, "No")}),
    Q2(2, "Are you happy?", new Answer[]{new Answer(4, "Yes"), new Answer(5, "No")}),
    Q3(3, "Are you sad?", new Answer[]{new Answer(6, "Yes"), new Answer(7, "No")}),
    Q4(4, "Is this a good question?", new Answer[]{new Answer(8, "Yes"), new Answer(9, "No")});

    private int id;
    private String question;
    private Answer[] answers;
    private List<Answer> selectedAnswers;

    /* private */ Question(int id, String question, Answer[] answers){
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
