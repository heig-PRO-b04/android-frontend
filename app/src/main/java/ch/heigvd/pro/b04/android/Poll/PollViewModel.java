package ch.heigvd.pro.b04.android.Poll;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Network.Rockin;
import ch.heigvd.pro.b04.android.Question.QuestionUtils;
import ch.heigvd.pro.b04.android.Utils.LocalDebug;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollViewModel extends AndroidViewModel {

    private MutableLiveData<Poll> poll = new MutableLiveData<>();
    private MutableLiveData<Question> questionToView = new MutableLiveData<>();

    private int idPoll;
    private int idModerator;
    private String token;

    private Callback<Poll> callbackPoll = new Callback<Poll>() {
        @Override
        public void onResponse(Call<Poll> call, Response<Poll> response) {
            if (response.isSuccessful()) {
                saveNewPoll(response.body());
            } else {
                LocalDebug.logUnsuccessfulRequest(call, response);
            }
        }

        @Override
        public void onFailure(Call<Poll> call, Throwable t) {
            LocalDebug.logFailedRequest(call, t);
        }
    };

    /**
     * Helper method used to save the new poll
     * @param newPoll The new poll to save
     */
    private void saveNewPoll(Poll newPoll) {
        poll.postValue(newPoll);
        sendGetQuestionRequest(newPoll);
    }

    /**
     * Helper method used to send a new request to get the questions
     */
    private void sendGetQuestionRequest(Poll poll)  {
        QuestionUtils.sendGetQuestionRequest(poll, token);
    }

    /**
     * Constructor
     */
    public PollViewModel(@NonNull Application application, String token, int idModerator, int idPoll) {
        super(application);
        this.token = token;
        this.idModerator = idModerator;
        this.idPoll = idPoll;
    }

    public MutableLiveData<Poll> getPoll() {
        return poll;
    }

    public void goToQuestion(Question question) {
        questionToView.postValue(question);
    }

    public MutableLiveData<List<Question>> getQuestions() {
        return QuestionUtils.getQuestions();
    }

    public MutableLiveData<Question> getQuestionToView() {
        return questionToView;
    }

    public void getPollFromBackend() {
        Rockin.api().getPollViaCall(idModerator, idPoll, token).enqueue(callbackPoll);
    }
}
