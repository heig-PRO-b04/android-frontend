package ch.heigvd.pro.b04.android.Poll;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Network.Rockin;
import ch.heigvd.pro.b04.android.Question.QuestionUtils;
import ch.heigvd.pro.b04.android.Utils.Exceptions.TokenNotSetException;
import ch.heigvd.pro.b04.android.Utils.LocalDebug;
import ch.heigvd.pro.b04.android.Utils.Persistent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Poll> poll = new MutableLiveData<>();
    private MutableLiveData<Question> questionToView = new MutableLiveData<>();

    private String idPoll;
    private String idModerator;

    private Callback<Poll> callbackPoll = new Callback<Poll>() {
        @Override
        public void onResponse(Call<Poll> call, Response<Poll> response) {
            if (response.isSuccessful()) {
                try {
                    saveNewPoll(response.body());
                } catch (TokenNotSetException e) {
                    LocalDebug.logTokenNotSet(e);
                }
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
     * @throws TokenNotSetException is thrown if the token doesn't exist
     */
    private void saveNewPoll(Poll newPoll) throws TokenNotSetException {
        poll.postValue(newPoll);
        sendGetQuestionRequest(newPoll);
    }

    /**
     * Helper method used to send a new request to get the questions
     * @throws TokenNotSetException is thrown if the token doesn't exist
     */
    private void sendGetQuestionRequest(Poll poll) throws TokenNotSetException {
        String token = Persistent.getStoredTokenOrError(context);
        QuestionUtils.sendGetQuestionRequest(poll, token);
    }

    /**
     * Constructor
     */
    public PollViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
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

    public void getPollFromBackend(String token) {
        Rockin.api().getPoll(idModerator, idPoll, token).enqueue(callbackPoll);
    }

    public void setIdPoll(String idPoll) {
        this.idPoll = idPoll;
    }

    public void setIdModerator(String idModerator) {
        this.idModerator = idModerator;
    }
}
