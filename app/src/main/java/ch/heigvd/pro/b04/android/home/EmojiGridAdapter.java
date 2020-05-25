package ch.heigvd.pro.b04.android.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.R;

public class EmojiGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_EMOJI = 1;

    private HomeViewModel state;

    public EmojiGridAdapter(HomeViewModel state) {
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
                ((EmojiViewHolder) holder).bindEmoji(Emoji.values()[position - 1]);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return Emoji.values().length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0
                ? VIEW_TYPE_HEADER
                : VIEW_TYPE_EMOJI;
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
}
