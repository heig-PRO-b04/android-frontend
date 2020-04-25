package ch.heigvd.pro.b04.android.Poll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Question.QuestionActivity;
import ch.heigvd.pro.b04.android.Poll.Question.QuestionAdapter;
import ch.heigvd.pro.b04.android.Poll.Question.Question;
import ch.heigvd.pro.b04.android.R;
import ch.heigvd.pro.b04.android.datamodel.Token;

public class PollActivity extends AppCompatActivity {
    private PollViewModel state;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Question> questions = new LinkedList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        Intent intent = getIntent();
        token = new Token(intent.getStringExtra("token"));

        //state = new PollViewModel(questions);
        state = new ViewModelProvider(this).get(PollViewModel.class);
        state.setToken(token);

        RecyclerView questionList = findViewById(R.id.poll_questions_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        QuestionAdapter questionAdapter = new QuestionAdapter(state, this);

        questionList.setAdapter(questionAdapter);
        questionList.setLayoutManager(manager);

        state.getQuestionToView().observe(this, question -> {
            Intent questionIntent = new Intent(this, QuestionActivity.class);
            questionIntent.putExtra("QUESTION_ID", question.getId());
            startActivity(questionIntent);
        });
    }

}
