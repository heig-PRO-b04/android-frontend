package ch.heigvd.pro.b04.android.Question;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.Question.Answer.AnswerAdapter;
import ch.heigvd.pro.b04.android.Poll.PollActivity;
import ch.heigvd.pro.b04.android.R;
import ch.heigvd.pro.b04.android.Utils.Exceptions.TokenNotSetException;
import ch.heigvd.pro.b04.android.Utils.Persistent;

public class QuestionActivity extends AppCompatActivity {
    private QuestionViewModel state;
    private String idPoll,  idModerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        Question question = (Question) intent.getSerializableExtra("question");
        String token = null;
        try {
            token = Persistent.getStoredTokenOrError(getApplicationContext());
        } catch (TokenNotSetException e) {
            finish();
        }

        state = new ViewModelProvider(this).get(QuestionViewModel.class);
        idModerator = question.getIdModerator();
        idPoll = question.getIdPoll();

        state.setQuestion(question);
        state.requestAnswers(token, question);

        RecyclerView answerList = findViewById(R.id.question_answers_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        AnswerAdapter answerAdapter = new AnswerAdapter(state, this);

        answerList.setAdapter(answerAdapter);
        answerList.setLayoutManager(manager);
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, QuestionActivity.class)
                .putExtra("question", state.getPreviousQuestion());
        startActivity(intent);
    }

    public void goNext(View view) {
        Intent intent = new Intent(this, QuestionActivity.class)
                .putExtra("question", state.getNextQuestion());
        startActivity(intent);
    }

    public void exitQuestion(View view) {
        Intent intent = new Intent(this, PollActivity.class);
        intent.putExtra("idPoll", idPoll);
        intent.putExtra("idModerator", idModerator);
        startActivity(intent);
    }
}
