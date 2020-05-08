package ch.heigvd.pro.b04.android.Question;

import android.content.Context;
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

import ch.heigvd.pro.b04.android.Datamodel.Answer;
import ch.heigvd.pro.b04.android.R;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ANSWER = 1;
    private static final long HEADER_ID = -1;

    private LifecycleOwner lifecycleOwner;
    private QuestionViewModel state;
    private Context context;

    private List<Answer> answers = new LinkedList<>();

    public QuestionAdapter(QuestionViewModel state, LifecycleOwner lifecycleOwner, Context context) {
        this.lifecycleOwner = lifecycleOwner;
        this.state = state;
        this.context = context;
        setHasStableIds(true);

        state.getCurrentAnswers().observe(lifecycleOwner, newAnswers -> {
            answers.clear();
            answers.addAll(newAnswers);
            notifyDataSetChanged();
        });
    }

    @Override
    public long getItemId(int position) {
        if (position == 0)
            return HEADER_ID;

        return answers.get(position - 1).getIdAnswer();
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public HeaderViewHolder(@NonNull ViewGroup parent, QuestionViewModel state, LifecycleOwner lifecycleOwner) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.question_title, parent, false));
            title = itemView.findViewById(R.id.question);

            state.getCurrentQuestion().observe(lifecycleOwner, selectedQuestion -> {
                title.setText(selectedQuestion.getTitle());
            });
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
            answerButton.setText(answer.getTitle());
            answerButton.setOnClickListener(v -> {
                state.selectAnswer(answer);
                if (answer.isSelected()) {
                    answerButton.setBackgroundColor(
                            context.getResources().getColor(R.color.colorSelected));
                } else {
                    answerButton.setBackgroundColor(Color.WHITE);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(parent, state, lifecycleOwner);
            case VIEW_TYPE_ANSWER:
                return new AnswerViewHolder(parent);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position != 0) {
            ((AnswerViewHolder) holder)
                    .bindAnswer(answers.get(position-1));
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
