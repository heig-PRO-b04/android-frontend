package ch.heigvd.pro.b04.android.Poll.Answer;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.PollViewModel;
import ch.heigvd.pro.b04.android.Poll.Question.Question;
import ch.heigvd.pro.b04.android.Poll.QuestionViewModel;
import ch.heigvd.pro.b04.android.R;

public class AnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ANSWER = 1;
    private QuestionViewModel state;

    private List<Answer> answers = new LinkedList<>();

    public AnswerAdapter(QuestionViewModel state, LifecycleOwner lifecycleOwner) {
        this.state = state;

        state.getAnswers().observe(lifecycleOwner, newAnswers -> {
            for (Answer a : newAnswers) {
                if (!answers.contains(a))
                    answers.add(a);
            }

            notifyDataSetChanged();
        });
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public HeaderViewHolder(@NonNull ViewGroup parent, QuestionViewModel state) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.question_title, parent, false));
            title = itemView.findViewById(R.id.question);
        }

        private void setTitle(String t) {
            title.setText(t);
        }
    }

    private class AnswerViewHolder extends RecyclerView.ViewHolder {
        private Button answerButton;

        private AnswerViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.question_answers, parent, false));

            answerButton = itemView.findViewById(R.id.question_answer_item);
        }

        private void bindAnswer(Answer answer) {
            answerButton.setText(answer.getAnswer());
            //answerButton.setOnClickListener(v -> answer.select());

            if (answer.isSelected()) {
                answerButton.setBackgroundColor(Color.GREEN);
            } else {
                answerButton.setBackgroundColor(Color.WHITE);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(parent, state);
            case VIEW_TYPE_ANSWER:
                return new AnswerViewHolder(parent);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                ((HeaderViewHolder) holder).setTitle(
                        state.getQuestion()
                );
                break;
            default:
                ((AnswerViewHolder) holder).bindAnswer(
                        answers.get(position-1)
                );
                break;
        }
    }

    @Override
    public int getItemCount() {
        return answers.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0
                ? VIEW_TYPE_HEADER
                : VIEW_TYPE_ANSWER;
    }

}
