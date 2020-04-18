package ch.heigvd.pro.b04.android.Poll.Question;

import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Answer.Answer;

public class Question {
    private int id;
    private String question;
    private int nummberOfAnswers;
    private Answer[] answers;
    private boolean answered;

    public Question(int id, String question, Answer[] answers){
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.answered = false;
        nummberOfAnswers = 1;
    }

    public Question(int id, String question, Answer[] answers, int numberOfAnswers){
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.answered = false;
        this.nummberOfAnswers = numberOfAnswers;
    }

    public void answer(Answer answer) {
        answer.select();
    }

    public String getQuestion() {
        return question;
    }

    public boolean answered() {
        return false;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public int getId() {
        return id;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        Question q = (Question) obj;
        return q.id == id &&
               this.question.equals(q.question) &&
               this.answers.equals(q.answers) &&
               q.nummberOfAnswers == nummberOfAnswers;
    }
}
