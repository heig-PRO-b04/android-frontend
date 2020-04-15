package ch.heigvd.pro.b04.android.Poll.Answer;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;

import ch.heigvd.pro.b04.android.Poll.PollViewModel;
import ch.heigvd.pro.b04.android.Poll.Question.Question;

public class AnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private PollViewModel state;
    private Set<Integer> selected = new HashSet<>();

    public AnswerAdapter(PollViewModel state, LifecycleOwner lifecycleOwner) {
        this.state = state;

        state.getAnsweredQuestion().observe(lifecycleOwner, questions -> {
            selected.clear();
            for (Question question : questions) {
                for (Answer answer : question.getSelectedAnswers() ) {
                    selected.add(answer.getId());
                }
            }
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
