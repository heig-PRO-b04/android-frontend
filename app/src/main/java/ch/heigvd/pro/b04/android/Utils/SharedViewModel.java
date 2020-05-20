package ch.heigvd.pro.b04.android.Utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Network.ApiResponse;
import ch.heigvd.pro.b04.android.Network.Rockin;

public abstract class SharedViewModel extends AndroidViewModel {
    private static int POLLING_DELAY = 1000;

    private LiveData<Poll> poll;
    private LiveData<List<Question>> questions;

    private MediatorLiveData<Boolean> responseError = new MediatorLiveData<>();

    public SharedViewModel(@NonNull Application application, String token, int idModerator, int idPoll) {
        super(application);

        // Poll
        LiveData<ApiResponse<Poll>> pollRequest = Rockin.api().getPoll(idModerator, idPoll, token);

        poll = Transformations.switchMap(
                pollRequest,
                response -> {
                    MutableLiveData<Poll> liveData = new MutableLiveData<>();
                    if (response.response().isPresent()) {
                        liveData.setValue(response.response().get());
                    }

                    return liveData;
                });

        // Questions
        LiveData<ApiResponse<List<Question>>> transformPoll = Transformations.switchMap(
                poll,
                poll -> Transformations.switchMap(
                            new PollingLiveData(POLLING_DELAY),
                            unit -> Rockin.api().getQuestions(poll, token)
                    )
        );

        questions = Transformations.distinctUntilChanged(Transformations.switchMap(
                transformPoll,
                response -> {
                    MutableLiveData<List<Question>> liveData = new MutableLiveData<>();
                    if (response.response().isPresent()) {
                        liveData.setValue(response.response().get());
                    }
                    return liveData;
                })
        );

        // Error
        LiveData<Boolean> pollError = Transformations.distinctUntilChanged(Transformations.map(
                pollRequest,
                response -> response.isFailure()
        ));

        LiveData<Boolean> questionError = Transformations.distinctUntilChanged(Transformations.map(
                transformPoll,
                response -> response.isFailure()
        ));

        responseError.addSource(pollError, response -> responseError.postValue(response));
        responseError.addSource(questionError, response -> responseError.postValue(response));
    }

    public LiveData<Poll> getPoll() {
        return poll;
    }

    public LiveData<Boolean> getErrors() {
        return responseError;
    }

    public LiveData<List<Question>> getQuestions() {
        return questions;
    }
}
