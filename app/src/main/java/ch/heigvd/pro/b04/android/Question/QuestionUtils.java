package ch.heigvd.pro.b04.android.Question;

import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Network.Rockin;
import ch.heigvd.pro.b04.android.Utils.LocalDebug;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionUtils {
    private static MutableLiveData<List<Question>> localQuestions =
            new MutableLiveData<>(new LinkedList<>());

    private QuestionUtils() {
    }

    private static Callback<List<Question>> callbackQuestions = new Callback<List<Question>>() {
        @Override
        public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
            if (response.isSuccessful()) {
                localQuestions.postValue(response.body());
            } else {
                LocalDebug.logUnsuccessfulRequest(call, response);
            }
        }

        @Override
        public void onFailure(Call<List<Question>> call, Throwable t) {
            LocalDebug.logFailedRequest(call, t);
        }
    };

    public static void sendGetQuestionRequest(Poll poll, String token) {
        Rockin.api()
                .getQuestions(poll.getIdModerator(), poll.getIdPoll(), token)
                .enqueue(callbackQuestions);
    }

    public static MutableLiveData<List<Question>> getQuestions() {
        return localQuestions;
    }
}
