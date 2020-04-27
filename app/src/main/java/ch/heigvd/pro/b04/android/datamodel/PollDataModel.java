package ch.heigvd.pro.b04.android.datamodel;

import com.google.gson.annotations.SerializedName;

public class PollDataModel {
    @SerializedName("idModerator")
    private String idModerator;

    @SerializedName("idPoll")
    private String idPoll;

    @SerializedName("title")
    private String title;


    public String getIdModerator() {
        return idModerator;
    }

    public String getIdPoll() {
        return idPoll;
    }

    public String getTitle() {
        return title;
    }
}
