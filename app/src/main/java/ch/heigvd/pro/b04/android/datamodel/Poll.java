package ch.heigvd.pro.b04.android.datamodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Poll implements Serializable {
    @SerializedName("idModerator")
    private int idModerator;

    @SerializedName("idPoll")
    private int idPoll;

    @SerializedName("title")
    private String title;

    public Poll(int idModerator, int idPoll) {
        this.idModerator = idModerator;
        this.idPoll = idPoll;
        this.title = "";
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
}
