package ch.heigvd.pro.b04.android.Poll.Question;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ch.heigvd.pro.b04.android.Poll.Answer.AnswerAdapter;
import ch.heigvd.pro.b04.android.Poll.PollActivity;
import ch.heigvd.pro.b04.android.R;

public class QuestionActivity extends AppCompatActivity {
    private QuestionViewModel state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        String idModerator = intent.getStringExtra("idModerator");
        String idPoll = intent.getStringExtra("idPoll");
        String idQuestion = intent.getStringExtra("idQuestion");
        String token = intent.getStringExtra("token");

        state = new ViewModelProvider(this).get(QuestionViewModel.class);
        state.getQuestion(idModerator, idPoll, idQuestion, token);

        RecyclerView answerList = findViewById(R.id.question_answers_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        AnswerAdapter answerAdapter = new AnswerAdapter(state, this);

        answerList.setAdapter(answerAdapter);
        answerList.setLayoutManager(manager);
/*
        state.getSelectedAnswers().observe(this, answers -> {
            for (Answer a: answers) {
                question.answer(a);
            }

            if (question.getNumberOfAnswers() == answers.size()) {
                question.answered();
                System.out.println("All needed answers selected");
            }

            System.out.println("An Answer was selected");
        });*/
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, QuestionActivity.class);
        // TODO : get the previous question
        startActivity(intent);
    }

    public void goNext(View view) {
        Intent intent = new Intent(this, QuestionActivity.class);
        // TODO : get the next question
        startActivity(intent);
    }

    public void exitQuestion(View view) {
        Intent intent = new Intent(this, PollActivity.class);
        startActivity(intent);
    }
}
