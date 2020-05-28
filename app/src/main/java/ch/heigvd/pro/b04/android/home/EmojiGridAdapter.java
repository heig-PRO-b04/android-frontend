package ch.heigvd.pro.b04.android.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.R;

public class EmojiGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_EMOJI = 1;
    private static final int VIEW_TYPE_INSTRUCTION = 2;

    private HomeViewModel state;

    EmojiGridAdapter(HomeViewModel state) {
        this.state = state;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(parent);
            case VIEW_TYPE_EMOJI:
                return new EmojiViewHolder(parent);
            case VIEW_TYPE_INSTRUCTION:
                return new InstructionViewHolder(parent);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position > 1) {
            ((EmojiViewHolder) holder).bindEmoji(Emoji.values()[position - 2]);
        }
    }

    @Override
    public int getItemCount() {
        return Emoji.values().length + 2;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0: return VIEW_TYPE_HEADER;
            case 1: return VIEW_TYPE_INSTRUCTION;
            default: return VIEW_TYPE_EMOJI;
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        HeaderViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_grid_title, parent, false));
        }
    }

    private static class InstructionViewHolder extends RecyclerView.ViewHolder {
        InstructionViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_instructions, parent, false));
            TextView title = itemView.findViewById(R.id.home_instructions_title);
            title.setText(R.string.connection_instruction);
        }
    }

    private class EmojiViewHolder extends RecyclerView.ViewHolder {
        private ImageView emojiButton;
        private CardView cardView;

        private EmojiViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_grid_item, parent, false));

            cardView = itemView.findViewById(R.id.home_grid_item);
            emojiButton = itemView.findViewById(R.id.home_grid_item_icon);
        }

        private void bindEmoji(Emoji emoji) {
            emojiButton.setImageResource(emoji.getEmoji());
            cardView.setOnClickListener(v -> state.addNewEmoji(emoji));
        }
    }
}
