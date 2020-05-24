package ch.heigvd.pro.b04.android.Question;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.Authentication.AuthenticationTokenLiveData;
import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.R;
import ch.heigvd.pro.b04.android.Utils.SharedViewModelFactory;

import static ch.heigvd.pro.b04.android.Poll.PollActivity.EXTRA_ID_MODERATOR;
import static ch.heigvd.pro.b04.android.Poll.PollActivity.EXTRA_ID_POLL;
import static ch.heigvd.pro.b04.android.Poll.PollActivity.EXTRA_QUESTION;
import static ch.heigvd.pro.b04.android.Poll.PollActivity.EXTRA_TOKEN;

public class QuestionActivity extends AppCompatActivity {

    private QuestionViewModel state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        final Intent intent = getIntent();
        final Question question = (Question) intent.getSerializableExtra(EXTRA_QUESTION);
        final int idModerator = intent.getIntExtra(EXTRA_ID_MODERATOR, 0);
        final int idPoll = intent.getIntExtra(EXTRA_ID_POLL, 0);
        final String token = getIntent().getStringExtra(EXTRA_TOKEN);

        state = new ViewModelProvider(this, new SharedViewModelFactory(
                getApplication(),
                idModerator,
                idPoll,
                token
        )).get(QuestionViewModel.class);

        state.setCurrentQuestion(question);
        state.getAllQuestionsFromBackend(new Poll(idModerator, idPoll), token);

        setupAnswerMinAlert(question);
        setupAnswerList();

        state.getResponseError().observe(this, gotError -> {
            if (gotError) {
                disconnect();
            }
        });
    }

    private void setupAnswerList() {
        RecyclerView answerList = findViewById(R.id.question_answers_view);
        answerList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager = new LinearLayoutManager(this);

        QuestionAdapter questionAdapter = new QuestionAdapter(state, this, getApplicationContext());

        answerList.setAdapter(questionAdapter);
        answerList.setLayoutManager(manager);
    }

    private void setupAnswerMinAlert(Question question) {
        TextView alert = findViewById(R.id.question_answers_alert);
        state.getNbCheckedAnswer().observe(this, nbrVotes -> {
            if (question.getAnswerMin() < nbrVotes ) {
                alert.setText(R.string.answers_min_alerts);
            } else {
                alert.setText("");
            }
        });
    }

    public void goBack(View view) {
        state.changeToPreviousQuestion();
    }

    public void goNext(View view) {
        state.changeToNextQuestion();
    }

    public void exitQuestion(View view) {
        finish();
    }

    private void disconnect() {
        AuthenticationTokenLiveData tokenLiveData = new AuthenticationTokenLiveData(getApplicationContext());
        tokenLiveData.logout();
        finish();
    }
}
