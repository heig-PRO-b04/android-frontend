package ch.heigvd.pro.b04.android.Poll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Question.QuestionActivity;
import ch.heigvd.pro.b04.android.Poll.Question.QuestionAdapter;
import ch.heigvd.pro.b04.android.Poll.Question.Question;
import ch.heigvd.pro.b04.android.R;
import ch.heigvd.pro.b04.android.datamodel.Poll;
import ch.heigvd.pro.b04.android.datamodel.Token;
import ch.heigvd.pro.b04.android.network.RetrofitClient;
import ch.heigvd.pro.b04.android.network.RockinAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollActivity extends AppCompatActivity {
    private PollViewModel state;
    private Token token;

    private Callback<Poll> callbackPoll = new Callback<Poll>() {
        @Override
        public void onResponse(Call<Poll> call, Response<Poll> response) {
            if (response.isSuccessful()) {
                Log.w("localDebug", call.request().url().toString());
                //poll.postValue(new Poll(response.body().getIdPoll(), response.body().getIdModerator(), response.body().getStatus()));
            } else {
                Log.w("localDebug", "Received error, HTTP status is " + response.code());
                Log.w("localDebug", "The request was " + call.request().url());

                try {
                    Log.w("localDebug", response.errorBody().string());
                } catch (IOException e) {
                    Log.e("localDebug", "Error in error, rip");
                }
            }
        }

        @Override
        public void onFailure(Call<Poll> call, Throwable t) {
            Log.e("localDebug", "We had a super bad error in callbackToken");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);
        Intent intent = getIntent();
        token = new Token(intent.getStringExtra("token"));

        //state = new PollViewModel(questions);
        state = new ViewModelProvider(this).get(PollViewModel.class);
        state.getSession(token);

        state.getPoll().observe(this, poll -> {
            if (poll != null) {
                RetrofitClient.getRetrofitInstance()
                        .create(RockinAPI.class)
                        .getPoll(poll.getIdModerator(), poll.getId(), token.getToken())
                        .enqueue(callbackPoll);
            }
        });

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
