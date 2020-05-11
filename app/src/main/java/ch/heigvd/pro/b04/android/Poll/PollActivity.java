package ch.heigvd.pro.b04.android.Poll;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.Authentication.AuthenticationTokenLiveData;
import ch.heigvd.pro.b04.android.Question.QuestionActivity;
import ch.heigvd.pro.b04.android.R;

public class PollActivity extends AppCompatActivity {

    public static final String EXTRA_ID_MODERATOR = "idModerator";
    public static final String EXTRA_ID_POLL = "idPoll";
    public static final String EXTRA_TOKEN = "token";

    private PollViewModel state;
    private AuthenticationTokenLiveData tokenLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        Intent intent = getIntent();

        tokenLiveData = new AuthenticationTokenLiveData(this);
        state = new ViewModelProvider(this, new PollViewModelFactory(
                getApplication(),
                intent.getStringExtra(EXTRA_TOKEN),
                intent.getIntExtra(EXTRA_ID_MODERATOR, 0),
                intent.getIntExtra(EXTRA_ID_POLL, 0)
        )).get(PollViewModel.class);

        state.getPollFromBackend();

        RecyclerView questionList = findViewById(R.id.poll_questions_view);
        questionList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager = new LinearLayoutManager(this);

        PollAdapter pollAdapter = new PollAdapter(state, this);

        questionList.setAdapter(pollAdapter);
        questionList.setLayoutManager(manager);

        state.getQuestionToView().observe(this, question -> {
            Intent questionIntent = new Intent(this, QuestionActivity.class)
                    .putExtra(QuestionActivity.EXTRA_TOKEN, intent.getStringExtra(EXTRA_TOKEN))
                    .putExtra("question", question)
                    .putExtra("poll", state.getPoll().getValue());

            startActivity(questionIntent);
        });
    }

    @Override
    public void onBackPressed() {
        tokenLiveData.logout();
        super.onBackPressed();
    }
}
