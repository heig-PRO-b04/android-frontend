package ch.heigvd.pro.b04.android.Home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.Poll.PollViewModel;
import ch.heigvd.pro.b04.android.R;

public class EmojiGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_EMOJI = 1;
    private static final int VIEW_TYPE_INSTRUCTION = 2;

    private HomeViewModel state;
    private LifecycleOwner lifecycleOwner;

    public EmojiGridAdapter(HomeViewModel state, LifecycleOwner lifecycleOwner) {
        this.state = state;
        this.lifecycleOwner = lifecycleOwner;
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
                return new InstructionViewHolder(parent, state, lifecycleOwner);
            default:
                throw new IllegalStateException("Unknown view type.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            default:
                ((EmojiViewHolder) holder).bindEmoji(Emoji.values()[position - 2]);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return Emoji.values().length + 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0
                ? VIEW_TYPE_HEADER
                : (position == 1
                    ? VIEW_TYPE_INSTRUCTION
                    :VIEW_TYPE_EMOJI);
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_grid_title, parent, false));
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

    private static class InstructionViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        public InstructionViewHolder(@NonNull ViewGroup parent, HomeViewModel state, LifecycleOwner lifecycleOwner) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_instructions, parent, false));
            title = itemView.findViewById(R.id.instructions);

            title.setText("In order to connect to a poll, please enter its emoji code or scan the given QR code.");
        }
    }
}
