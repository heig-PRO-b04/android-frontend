package ch.heigvd.pro.b04.android.Datamodel;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Answer {
    @SerializedName("idModerator")
    private String idModerator;

    @SerializedName("idPoll")
    private String idPoll;

    @SerializedName("idQuestion")
    private long idQuestion;

    @SerializedName("idAnswer")
    private long idAnswer;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("checked")
    private boolean checked;

    public String getIdModerator() {
        return idModerator;
    }

    public String getIdPoll() {
        return idPoll;
    }

    public long getIdQuestion() {
        return idQuestion;
    }

    public long getIdAnswer() {
        return idAnswer;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void toggle() {
        checked = !checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;
        return  Objects.equals(idModerator, answer.idModerator) &&
                Objects.equals(idPoll, answer.idPoll) &&
                Objects.equals(idQuestion, answer.idQuestion) &&
                Objects.equals(title, answer.title) &&
                Objects.equals(description, answer.description);
    }
}
