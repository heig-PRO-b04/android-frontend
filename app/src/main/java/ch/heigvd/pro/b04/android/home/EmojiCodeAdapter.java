package ch.heigvd.pro.b04.android.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.R;
public class EmojiCodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HomeViewModel state;
    private List<Emoji> emojis = new LinkedList<>();

    public EmojiCodeAdapter(HomeViewModel state, LifecycleOwner lifecycleOwner) {
        this.state = state;

        state.getCodeEmoji().observe(lifecycleOwner, emojis -> {
            this.emojis.clear();
            this.emojis.addAll(emojis);
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmojiViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((EmojiViewHolder) holder).bindEmoji(
                emojis.get(position)
        );
    }

    @Override
    public int getItemCount() {
        return emojis.size();
    }

    private static class EmojiViewHolder extends RecyclerView.ViewHolder {
        private ImageView emojiImage;

        private EmojiViewHolder(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_code_item, parent, false));

            emojiImage = itemView.findViewById(R.id.home_code_item);
        }

        private void bindEmoji(Emoji emoji) {
            this.emojiImage.setImageResource(emoji.getEmoji());
            this.emojiImage.setBackground(null);
        }
    }
}
