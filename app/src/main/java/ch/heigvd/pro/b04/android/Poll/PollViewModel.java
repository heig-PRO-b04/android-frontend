package ch.heigvd.pro.b04.android.Poll;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Network.Rockin;
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
    private MutableLiveData<List<Question>> questions = new MutableLiveData<>(new LinkedList<>());

    /**********************
     * Callback variables *
     **********************/
    private Callback<List<Question>> callbackQuestions = new Callback<List<Question>>() {
        @Override
        public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
            if (response.isSuccessful()) {
                questions.postValue(response.body());
            } else {
                LocalDebug.logUnsuccessfulRequest(call, response);
            }
        }

        @Override
        public void onFailure(Call<List<Question>> call, Throwable t) {
            LocalDebug.logFailedRequest(call, t);
        }
    };
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
        sendGetQuestionRequest();
    }

    /**
     * Helper method used to send a new request to get the questions
     * @throws TokenNotSetException is thrown if the token doesn't exist
     */
    private void sendGetQuestionRequest() throws TokenNotSetException {
        String token = Persistent.getStoredTokenOrError(context);

        Rockin.api()
                .getQuestions(poll.getValue().getIdModerator(), poll.getValue().getIdPoll(), token)
                .enqueue(callbackQuestions);
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
        System.out.println("Question was selected...\n");
        questionToView.postValue(question);
    }

    public MutableLiveData<List<Question>> getQuestions() {
        return questions;
    }

    public MutableLiveData<Question> getQuestionToView() {
        return questionToView;
    }

    public void getPoll(String idPoll, String idModerator, String token) {
        Rockin.api().getPoll(idModerator, idPoll, token).enqueue(callbackPoll);
    }
}
