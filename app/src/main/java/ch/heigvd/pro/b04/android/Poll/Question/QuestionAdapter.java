package ch.heigvd.pro.b04.android.Poll.Question;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;

import ch.heigvd.pro.b04.android.Poll.PollViewModel;
import ch.heigvd.pro.b04.android.R;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_QUESTION = 1;

    private PollViewModel state;

    public QuestionAdapter(PollViewModel state, LifecycleOwner lifecycleOwner) {
        this.state = state;
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.poll_title, parent, false));
        }
    }

    private class QuestionViewHolder extends RecyclerView.ViewHolder {
        private Button questionButton;

        private QuestionViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.poll_question, parent, false));

            questionButton = itemView.findViewById(R.id.poll_question_item);
        }

        private void bindQuestion(Question question, boolean answered) {
            questionButton.setText(question.getQuestion());
            questionButton.setOnClickListener(v -> state.goToQuestion(question));

            if (answered) {
                questionButton.setBackgroundColor(Color.GREEN);
            } else {
                questionButton.setBackgroundColor(Color.WHITE);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(parent);
            case VIEW_TYPE_QUESTION:
                return new QuestionViewHolder(parent);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                break;
            default:
                Question q = Question.values()[position-1];
                ((QuestionViewHolder) holder).bindQuestion(
                        q,
                        q.answered()
                );
                break;
        }
    }

    @Override
    public int getItemCount() {
        return Question.values().length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0
                ? VIEW_TYPE_HEADER
                : VIEW_TYPE_QUESTION;
    }

}
