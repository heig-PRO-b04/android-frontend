package ch.heigvd.pro.b04.android.Datamodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Question implements Serializable {
    @SerializedName("idModerator")
    private String idModerator;

    @SerializedName("idPoll")
    private String idPoll;

    @SerializedName("idQuestion")
    private String idQuestion;

    @SerializedName("indexInPoll")
    private double indexInPoll;

    @SerializedName("title")
    private String title;

    @SerializedName("details")
    private String details;

    @SerializedName("answerMin")
    private String answerMin;

    @SerializedName("answerMax")
    private String answerMax;

    private boolean answered;

    public String getIdModerator() {
        return idModerator;
    }

    public String getIdPoll() {
        return idPoll;
    }

    public String getIdQuestion() {
        return idQuestion;
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
        return answered;
    }

    public double getIndexInPoll() {
        return indexInPoll;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Double.compare(question.indexInPoll, indexInPoll) == 0 &&
                answered == question.answered &&
                Objects.equals(idModerator, question.idModerator) &&
                Objects.equals(idPoll, question.idPoll) &&
                Objects.equals(idQuestion, question.idQuestion) &&
                Objects.equals(title, question.title) &&
                Objects.equals(details, question.details) &&
                Objects.equals(answerMin, question.answerMin) &&
                Objects.equals(answerMax, question.answerMax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idModerator, idPoll, idQuestion, indexInPoll, title, details, answerMin, answerMax, answered);
    }
}
