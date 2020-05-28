package ch.heigvd.pro.b04.android.datamodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Question implements Serializable {
    @SerializedName("idModerator")
    private long idModerator;

    @SerializedName("idPoll")
    private long idPoll;

    @SerializedName("idQuestion")
    private long idQuestion;

    @SerializedName("indexInPoll")
    private double indexInPoll;

    @SerializedName("title")
    private String title;

    @SerializedName("details")
    private String details;

    @SerializedName("answerMin")
    private int answerMin;

    @SerializedName("answerMax")
    private int answerMax;

    public long getIdModerator() {
        return idModerator;
    }

    public long getIdPoll() {
        return idPoll;
    }

    public long getIdQuestion() {
        return idQuestion;
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

    public double getIndexInPoll() {
        return indexInPoll;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return  Objects.equals(idModerator, question.idModerator) &&
                Objects.equals(idPoll, question.idPoll) &&
                Objects.equals(idQuestion, question.idQuestion) &&
                Objects.equals(title, question.title) &&
                Objects.equals(details, question.details) &&
                Objects.equals(answerMin, question.answerMin) &&
                Objects.equals(answerMax, question.answerMax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idModerator, idPoll, idQuestion, indexInPoll, title, details, answerMin, answerMax);
    }
}
