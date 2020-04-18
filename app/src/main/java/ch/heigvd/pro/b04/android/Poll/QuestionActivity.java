package ch.heigvd.pro.b04.android.Poll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import ch.heigvd.pro.b04.android.R;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        int id = intent.getIntExtra("QUESTION_ID", 0);

        System.out.println("Question id : " + id);

        //TODO : get question from backend (from id)
    }
}
