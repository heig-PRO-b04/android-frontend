package ch.heigvd.pro.b04.android.Poll;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.Question.QuestionActivity;
import ch.heigvd.pro.b04.android.R;
import ch.heigvd.pro.b04.android.Utils.Exceptions.TokenNotSetException;
import ch.heigvd.pro.b04.android.Utils.Persistent;

public class PollActivity extends AppCompatActivity {
    private PollViewModel state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        Intent intent = getIntent();

        String token = null;
        try {
            token = Persistent.getStoredTokenOrError(getApplicationContext());
        } catch (TokenNotSetException e) {
            finish();
        }

        state = new ViewModelProvider(this).get(PollViewModel.class);

        state.setIdPoll(intent.getStringExtra("idPoll"));
        state.setIdModerator(intent.getStringExtra("idModerator"));

        state.getPollFromBackend(token);

        RecyclerView questionList = findViewById(R.id.poll_questions_view);
        questionList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager = new LinearLayoutManager(this);

        PollAdapter pollAdapter = new PollAdapter(state, this);

        questionList.setAdapter(pollAdapter);
        questionList.setLayoutManager(manager);

        state.getQuestionToView().observe(this, question -> {
            Intent questionIntent = new Intent(this, QuestionActivity.class)
                    .putExtra("question", question)
                    .putExtra("poll", state.getPoll().getValue());

            startActivity(questionIntent);
        });
    }

}
