package ch.heigvd.pro.b04.android.Question;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.R;
import ch.heigvd.pro.b04.android.Utils.Exceptions.TokenNotSetException;
import ch.heigvd.pro.b04.android.Utils.Persistent;

public class QuestionActivity extends AppCompatActivity {
    private QuestionViewModel state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        Question question = (Question) intent.getSerializableExtra("question");
        Poll poll = (Poll) intent.getSerializableExtra("poll");

        String tmptoken = null;
        try {
            tmptoken = Persistent.getStoredTokenOrError(getApplicationContext());
        } catch (TokenNotSetException e) {
            finish();
        }
        final String token = tmptoken;

        state = new ViewModelProvider(this).get(QuestionViewModel.class);

        state.getCurrentQuestion().observe(this, q -> state.setAnswers(token, q));

        state.setCurrentQuestion(question);
        state.getAllQuestionsFromBackend(poll, token);

        RecyclerView answerList = findViewById(R.id.question_answers_view);
        answerList.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager = new LinearLayoutManager(this);

        QuestionAdapter questionAdapter = new QuestionAdapter(state, this);

        answerList.setAdapter(questionAdapter);
        answerList.setLayoutManager(manager);
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
}
