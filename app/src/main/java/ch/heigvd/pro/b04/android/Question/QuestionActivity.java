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

public class QuestionActivity extends AppCompatActivity {

    public static final String EXTRA_TOKEN = "token";

    private QuestionViewModel state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        Question question = (Question) intent.getSerializableExtra("question");
        Poll poll = (Poll) intent.getSerializableExtra("poll");

        final String token = getIntent().getStringExtra(EXTRA_TOKEN);

        state = new ViewModelProvider(this).get(QuestionViewModel.class);
        state.setCurrentQuestion(question);
        state.getAllQuestionsFromBackend(poll, token);

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
