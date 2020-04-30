package ch.heigvd.pro.b04.android.Poll.Question;

import androidx.annotation.Nullable;

public class Question {
    private String
            idModerator,
            idPoll,
            idQuestion,
            title,
            details,
            answerMin,
            answerMax;

    public Question(String idModerator, String idPoll, String idQuestion,
                    String title, String details,
                    String answerMin, String answerMax) {
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
        if (obj == null)
            return false;
        Question q = (Question) obj;
        return (q.idModerator == idModerator &&
                q.answerMax == answerMax &&
                q.answerMin == answerMin &&
                q.idPoll == idPoll &&
                q.idQuestion == idQuestion &&
                q.title.equals(title) &&
                q.details.equals(details));
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public String getIdModerator() {
        return idModerator;
    }

    public String getIdPoll() {
        return idPoll;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getAnswerMin() {
        return answerMin;
    }

    public String getAnswerMax() {
        return answerMax;
    }

    public boolean answered() {
        return false;
    }
}
