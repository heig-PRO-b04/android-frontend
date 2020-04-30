package ch.heigvd.pro.b04.android.Datamodel;

import com.google.gson.annotations.SerializedName;

public class Session {
    @SerializedName("idModerator")
    private String idModerator;

    @SerializedName("idPoll")
    private String idPoll;

    @SerializedName("idSession")
    private String idSession;

    @SerializedName("code")
    private String code;

    @SerializedName("status")
    private String status;

    public Session(String idModerator, String idPoll, String idSession, String code, String status) {
        this.idModerator = idModerator;
        this.idPoll = idPoll;
        this.idSession = idSession;

        this.code = code;
        this.status = status;
    }

    public String getIdModerator() {
        return idModerator;
    }

    public String getIdPoll() {
        return idPoll;
    }

    public String getIdSession() {
        return idSession;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
