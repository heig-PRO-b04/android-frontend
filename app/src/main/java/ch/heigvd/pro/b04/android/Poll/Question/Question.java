package ch.heigvd.pro.b04.android.Poll.Question;

import androidx.annotation.Nullable;

public class Question {
    private int
            idModerator,
            idPoll,
            idQuestion;
    private String
            title,
            details;
    private int
            answerMin,
            answerMax;

    public Question(int idModerator, int idPoll, int idQuestion,
                    String title, String details,
                    int answerMin, int answerMax) {
        this.idModerator = idModerator;
        this.idPoll = idPoll;
        this.idQuestion = idQuestion;

        this.title = title;
        this.details = details;

        this.answerMin = answerMin;
        this.answerMax = answerMax;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Question q = (Question) obj;
        return (q.idModerator == idModerator &&
                q.answerMax == answerMax &&
                q.answerMin == answerMin &&
                q.idPoll == idPoll &&
                q.idQuestion == idQuestion &&
                q.title.equals(title) &&
                q.details.equals(details));
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public int getIdModerator() {
        return idModerator;
    }

    public int getIdPoll() {
        return idPoll;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public int getAnswerMin() {
        return answerMin;
    }

    public int getAnswerMax() {
        return answerMax;
    }

    public boolean answered() {
        return false;
    }
}
