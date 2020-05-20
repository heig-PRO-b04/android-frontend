package ch.heigvd.pro.b04.android.Poll;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.Authentication.AuthenticationTokenLiveData;
import ch.heigvd.pro.b04.android.Question.QuestionActivity;
import ch.heigvd.pro.b04.android.R;
import ch.heigvd.pro.b04.android.Utils.SharedViewModelFactory;

public class PollActivity extends AppCompatActivity {

    public static final String EXTRA_ID_MODERATOR = "idModerator";
    public static final String EXTRA_ID_POLL = "idPoll";
    public static final String EXTRA_TOKEN = "token";
    public static final String EXTRA_QUESTION = "question";
    public static final String EXTRA_POLL = "poll";

    private PollViewModel state;
    private AuthenticationTokenLiveData tokenLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        Intent intent = getIntent();

        tokenLiveData = new AuthenticationTokenLiveData(this);
        state = new ViewModelProvider(this, new SharedViewModelFactory(
                getApplication(),
                intent.getIntExtra(EXTRA_ID_MODERATOR, 0),
                intent.getIntExtra(EXTRA_ID_POLL, 0),
                intent.getStringExtra(EXTRA_TOKEN)
        )).get(PollViewModel.class);

        RecyclerView questionList = findViewById(R.id.poll_questions_view);
        questionList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager = new LinearLayoutManager(this);

        PollAdapter pollAdapter = new PollAdapter(state, this);

        questionList.setAdapter(pollAdapter);
        questionList.setLayoutManager(manager);

        state.getQuestionToView().observe(this, question -> {
            Intent questionIntent = new Intent(this, QuestionActivity.class)
                    .putExtra(EXTRA_TOKEN, intent.getStringExtra(EXTRA_TOKEN))
                    .putExtra(EXTRA_QUESTION, question)
                    .putExtra(EXTRA_POLL, state.getPoll().getValue());

            startActivity(questionIntent);
        });

        Button exitButton = findViewById(R.id.poll_exit_button);
        exitButton.setOnClickListener(view -> disconnectFromPoll());

        state.getErrors().observe(this, isError -> {
            if (isError) {
                disconnectFromPoll();
            }
        });

        tokenLiveData.observe(this, s -> {
            if (! s.isPresent()) {
                disconnectFromPoll();
            }
        });
    }

    /**
     * Helper method that disconnects us from the current Poll
     */
    private void disconnectFromPoll() {
        tokenLiveData.logout();
        finish();
    }

    @Override
    public void onBackPressed() {
        tokenLiveData.logout();
        super.onBackPressed();
    }
}
