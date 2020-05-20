package ch.heigvd.pro.b04.android.Poll;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Question;
import ch.heigvd.pro.b04.android.R;

public class PollAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_QUESTION = 1;
    private static final int VIEW_TYPE_INSTRUCTION = 2;
    private static final long HEADER_ID = -1;
    private static final long INSTRUCTION_ID = -2;

    private PollViewModel state;
    private LifecycleOwner lifecycleOwner;

    private List<Question> questions = new LinkedList<>();

    public PollAdapter(PollViewModel state, LifecycleOwner lifecycleOwner) {
        this.state = state;
        this.lifecycleOwner = lifecycleOwner;
        setHasStableIds(true);

        state.getQuestions().observe(lifecycleOwner, newQuestions -> {
            questions = newQuestions;
            Collections.sort(questions, (o1, o2) -> Double.compare(o1.getIndexInPoll(), o2.getIndexInPoll()));

            notifyDataSetChanged();
        });
    }

    @Override
    public long getItemId(int position) {
        if (position == 0)
            return HEADER_ID;
        if (position == 1)
            return INSTRUCTION_ID;

        return questions.get(position - 2).getIdQuestion();
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        public HeaderViewHolder(@NonNull ViewGroup parent, PollViewModel state, LifecycleOwner lifecycleOwner) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.poll_title, parent, false));
            title = itemView.findViewById(R.id.poll_title);

            state.getPoll().observe(lifecycleOwner, poll -> {
                title.setText(poll.getTitle());
            });
        }
    }

    private static class InstructionViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        public InstructionViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_instructions, parent, false));
            title = itemView.findViewById(R.id.instructions);

            title.setText("In order to answer a question, please click on it.");
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
            questionButton.setText(question.getTitle());
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
                return new HeaderViewHolder(parent, state, lifecycleOwner);
            case VIEW_TYPE_QUESTION:
                return new QuestionViewHolder(parent);
            case VIEW_TYPE_INSTRUCTION:
                return new InstructionViewHolder(parent);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position != 0 && position != 1) {
            Question q = questions.get(position-2);
            ((QuestionViewHolder) holder).bindQuestion(q, q.answered() );
        }
    }

    @Override
    public int getItemCount() {
        return questions.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0
                ? VIEW_TYPE_HEADER
                : (position == 1
                    ? VIEW_TYPE_INSTRUCTION
                    : VIEW_TYPE_QUESTION);
    }

}
