package ch.heigvd.pro.b04.android.Question;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.heigvd.pro.b04.android.Datamodel.Answer;
import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Network.Rockin;
import ch.heigvd.pro.b04.android.Utils.LocalDebug;
import ch.heigvd.pro.b04.android.Utils.PollingLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionViewModel extends ViewModel {
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
    private MediatorLiveData<List<Answer>> currentAnswers = new MediatorLiveData<>();

    public QuestionViewModel() {
        LiveData<List<Answer>> transformQuestion = Transformations.switchMap(
                currentQuestion,
                question -> questionToAnswer(question, token)
        );

        LiveData<List<Answer>> transformDelayed = Transformations.switchMap(
                new PollingLiveData(POLLING_DELAY),
                unit -> Transformations.map(
                        questionToAnswer(currentQuestion.getValue(), token),
                        input -> {
                            List<Answer> transferred = new ArrayList<>();
                            for (Answer received : input) {
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

                            return transferred;
                        })
                );

        currentAnswers.addSource(transformQuestion, answers -> currentAnswers.postValue(answers));
        currentAnswers.addSource(transformDelayed, answers -> currentAnswers.postValue(answers));
    }

    private static LiveData<List<Answer>> questionToAnswer(Question question, String token) {
        return Rockin.api().getAnswers(
                question.getIdModerator(),
                question.getIdPoll(),
                question.getIdQuestion(),
                token);
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

    public MutableLiveData<List<Answer>> getCurrentAnswers() {
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

        for (Question q : QuestionUtils.getQuestions().getValue()) {
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

        if (  question.getAnswerMax() > counter
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
            Rockin.api().voteForAnswer(answer, token).enqueue(callbackVote);
        }
    }

    public MutableLiveData<Integer> getNbCheckedAnswer() {
        return nbCheckedAnswer;
    }

}
