package ch.heigvd.pro.b04.android.Poll;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.Poll.Question.QuestionActivity;
import ch.heigvd.pro.b04.android.Poll.Question.QuestionAdapter;
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
        String idPoll = intent.getStringExtra("idPoll");
        String idModerator = intent.getStringExtra("idModerator");

        String token = null;
        try {
            token = Persistent.getStoredTokenOrError(getApplicationContext());
        } catch (TokenNotSetException e) {
            finish();
        }

        state = new ViewModelProvider(this).get(PollViewModel.class);
        state.getPoll(idPoll, idModerator, token);

        RecyclerView questionList = findViewById(R.id.poll_questions_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        QuestionAdapter questionAdapter = new QuestionAdapter(state, this);

        questionList.setAdapter(questionAdapter);
        questionList.setLayoutManager(manager);

        state.getQuestionToView().observe(this, question -> {
            Intent questionIntent = new Intent(this, QuestionActivity.class);

            questionIntent.putExtra("idModerator", question.getIdModerator());
            questionIntent.putExtra("idPoll", question.getIdPoll());
            questionIntent.putExtra("idQuestion", question.getIdQuestion());

            startActivity(questionIntent);
        });
    }

}
