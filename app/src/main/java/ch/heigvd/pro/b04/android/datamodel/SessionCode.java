package ch.heigvd.pro.b04.android.datamodel;

import com.google.gson.annotations.SerializedName;

public class SessionCode {
    @SerializedName("code")
    private String code;

    public SessionCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
