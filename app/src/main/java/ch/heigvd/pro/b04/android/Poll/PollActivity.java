package ch.heigvd.pro.b04.android.Poll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Answer.Answer;
import ch.heigvd.pro.b04.android.Poll.Question.QuestionActivity;
import ch.heigvd.pro.b04.android.Poll.Question.QuestionAdapter;
import ch.heigvd.pro.b04.android.Poll.Question.Question;
import ch.heigvd.pro.b04.android.R;

public class PollActivity extends AppCompatActivity {
    private PollViewModel state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Question> questions = new LinkedList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        Question q0 = new Question(0, "Is it sunny today?", new Answer[]{new Answer(0, "Yes"), new Answer(1, "No")});
        Question q1 = new Question(1, "Is it rainy today?", new Answer[]{new Answer(2, "Yes"), new Answer(3, "No")});
        Question q2 = new Question(2, "Are you happy?", new Answer[]{new Answer(4, "Yes"), new Answer(5, "No")});
        Question q3 = new Question(3, "Are you sad?", new Answer[]{new Answer(6, "Yes"), new Answer(7, "No")});
        Question q4 = new Question(4, "Is this a good question?", new Answer[]{new Answer(8, "Yes"), new Answer(9, "No")});

        questions.add(q0);
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        questions.add(q4);

        //state = new PollViewModel(questions);
        state = new ViewModelProvider(this).get(PollViewModel.class);
        state.addQuestion(q0);
        state.addQuestion(q1);
        state.addQuestion(q2);
        state.addQuestion(q3);
        state.addQuestion(q4);

        RecyclerView questionList = findViewById(R.id.poll_questions_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        QuestionAdapter questionAdapter = new QuestionAdapter(state, this);

        questionList.setAdapter(questionAdapter);
        questionList.setLayoutManager(manager);

        state.getQuestionToView().observe(this, question -> {
            Intent intent = new Intent(this, QuestionActivity.class);
            intent.putExtra("QUESTION_ID", question.getId());
            startActivity(intent);
        });
    }

}
