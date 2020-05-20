package ch.heigvd.pro.b04.android.Poll;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Utils.SharedViewModel;

public class PollViewModel extends SharedViewModel {
    private MutableLiveData<Question> questionToView = new MutableLiveData<>();

    public PollViewModel(@NonNull Application application, int idModerator, int idPoll, String token) {
        super(application, idModerator, idPoll, token);
    }

    public void goToQuestion(Question question) {
        questionToView.postValue(question);
    }

    public LiveData<Question> getQuestionToView() {
        return questionToView;
    }
}
