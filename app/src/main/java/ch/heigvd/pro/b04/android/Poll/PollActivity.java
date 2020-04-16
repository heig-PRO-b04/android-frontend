package ch.heigvd.pro.b04.android.Poll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Question.QuestionAdapter;
import ch.heigvd.pro.b04.android.Poll.Question.Question;
import ch.heigvd.pro.b04.android.R;

public class PollActivity extends AppCompatActivity {
    private PollViewModel state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        state = new ViewModelProvider(this).get(PollViewModel.class);

        RecyclerView questionGrid = findViewById(R.id.poll_questions_view);
        GridLayoutManager manager = new GridLayoutManager(this, 1);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        QuestionAdapter questionAdapter = new QuestionAdapter(state, this);

        questionGrid.setAdapter(questionAdapter);
        questionGrid.setLayoutManager(manager);

    }
}
