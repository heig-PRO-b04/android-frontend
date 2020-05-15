package ch.heigvd.pro.b04.android.Network;

import ch.heigvd.pro.b04.android.Datamodel.Answer;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class Rockin {
    private static RockinAPI rockinAPI;

    public static RockinAPI api() {
        if (rockinAPI == null) {
            rockinAPI = RetrofitClient.getRetrofitInstance().create(RockinAPI.class);
        }

        return rockinAPI;
    }

    public static Call<ResponseBody> voteForAnswer(Answer answer, String token) {
        return Rockin.api().voteForAnswer(
                answer.getIdModerator(),
                answer.getIdPoll(),
                answer.getIdQuestion(),
                answer.getIdAnswer(),
                token,
                answer);
    }
}
