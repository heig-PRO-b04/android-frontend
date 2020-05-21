package ch.heigvd.pro.b04.android.Question;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.heigvd.pro.b04.android.Datamodel.Answer;
import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Network.ApiResponse;
import ch.heigvd.pro.b04.android.Network.RockinAPI;
import ch.heigvd.pro.b04.android.Utils.LocalDebug;
import ch.heigvd.pro.b04.android.Utils.PollingLiveData;
import ch.heigvd.pro.b04.android.Utils.SharedViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionViewModel extends SharedViewModel {
    private static int POLLING_DELAY = 1000;
    private static int STABILIZATION_DELAY = 7500;

    /**
     * The idea is to store the time when we last sent a request to the server. We will not accept
     * new information from the server until a certain delay (STABILIZATION_DELAY). This guarantees
     * a smoother UI refreshing for the user as the server will never invalidate something the user
     * just entered.
     */
    private Map<Answer, Long> lastRequest = new HashMap<>();

    private String token;
    private MutableLiveData<Integer> nbCheckedAnswer = new MutableLiveData<>(0);
    private MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    private LiveData<List<Answer>> currentAnswers = new MutableLiveData<>();
    private MediatorLiveData<Boolean> responseError = new MediatorLiveData<>();

    public QuestionViewModel(@NonNull Application application, int idModerator, int idPoll, String token) {
        super(application, idModerator, idPoll, token);

        LiveData<ApiResponse<List<Answer>>> transformQuestion = Transformations.switchMap(
                currentQuestion,
                question -> Transformations.switchMap(
                        new PollingLiveData(POLLING_DELAY),
                        unit -> RockinAPI.Companion.getAnswers(question, token)
                )
        );

        currentAnswers = Transformations.switchMap(transformQuestion, response -> {
            if (! response.response().isPresent()) {
                return new MutableLiveData<>();
            }

            List<Answer> transferred = new ArrayList<>();
            for (Answer received : response.response().get()) {
                Long delta = System.currentTimeMillis() - lastRequest.getOrDefault(received, 0L);
                // If enough time has passed, update the answer
                if (delta > STABILIZATION_DELAY) {
                    transferred.add(received);
                    lastRequest.remove(received);
                } else {
                    // If not enough time has passed, use the last known state
                    currentAnswers.getValue().stream()
                            .filter(ans -> ans.equals(received))
                            .findFirst()
                            .ifPresent(transferred::add);
                }
            }

            return new MutableLiveData<>(transferred);
        });

        LiveData<Boolean> answerError = Transformations.distinctUntilChanged(Transformations.map(
                transformQuestion,
                response -> response.isFailure()
        ));

        responseError.addSource(getErrors(), response -> responseError.postValue(response));
        responseError.addSource(answerError, response -> responseError.postValue(response));
    }

    private Callback<ResponseBody> callbackVote = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (!response.isSuccessful()) {
                LocalDebug.logUnsuccessfulRequest(call, response);
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            LocalDebug.logFailedRequest(call, t);
        }
    };

    public LiveData<List<Answer>> getCurrentAnswers() {
        return currentAnswers;
    }

    public void getAllQuestionsFromBackend(Poll poll, String token) {
        this.token = token;
        QuestionUtils.sendGetQuestionRequest(poll, token);
    }

    public LiveData<Question> getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question question) {
        currentQuestion.postValue(question);
    }

    public void changeToPreviousQuestion() {
        double currentIndex = currentQuestion.getValue().getIndexInPoll();
        double candidateIndex = Double.MIN_VALUE;
        Question candidate = null;

        for (Question q : getQuestions().getValue()) {
            double newIndex = q.getIndexInPoll();
            if (newIndex < currentIndex && newIndex > candidateIndex) {
                candidateIndex = newIndex;
                candidate = q;
            }
        }

        if (candidate != null)
            currentQuestion.setValue(candidate);
    }

    public void changeToNextQuestion() {
        double currentIndex = currentQuestion.getValue().getIndexInPoll();
        double candidateIndex = Double.MAX_VALUE;
        Question candidate = null;

        for (Question q : QuestionUtils.getQuestions().getValue()) {
            double newIndex = q.getIndexInPoll();
            if (newIndex > currentIndex && newIndex < candidateIndex) {
                candidateIndex = newIndex;
                candidate = q;
            }
        }

        if (candidate != null)
            currentQuestion.setValue(candidate);
    }

    public void selectAnswer(Answer answer) {
        Question question = currentQuestion.getValue();

        if (question == null || question.getIdQuestion() != answer.getIdQuestion())
            return;

        int counter = nbCheckedAnswer.getValue();

        if (question.getAnswerMax() > counter
                || question.getAnswerMax() == 0
                || answer.isChecked()) {

            if (answer.isChecked()) {
                counter++;
            } else {
                counter--;
            }
            nbCheckedAnswer.setValue(counter);

            answer.toggle();
            // Save at which time we sent the request
            lastRequest.put(answer, System.currentTimeMillis());
            RockinAPI.Companion.voteForAnswer(answer, token).enqueue(callbackVote);
        }
    }

    public MutableLiveData<Integer> getNbCheckedAnswer() {
        return nbCheckedAnswer;
    }

    public LiveData<Boolean> getResponseError() {
        return responseError;
    }

}
