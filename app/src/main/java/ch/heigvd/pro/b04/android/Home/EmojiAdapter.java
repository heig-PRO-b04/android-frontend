package ch.heigvd.pro.b04.android.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.emoji.widget.EmojiButton;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.R;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
    private HomeViewModel state;

    public EmojiAdapter(HomeViewModel state) {
        this.state = state;
    }

    @NonNull
    @Override
    public EmojiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View emojiView = inflater.inflate(R.layout.home_emoji, parent, false);
        ViewHolder viewHolder = new ViewHolder(emojiView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiAdapter.ViewHolder holder, int position) {
        EmojiButton button = holder.emojiButton;
        Emoji emoji = Emoji.at(position);

        button.setText(emoji.getEmoji());
        button.setOnClickListener(v -> state.addNewEmoji(emoji));
    }

    @Override
    public int getItemCount() {
        return Emoji.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public EmojiButton emojiButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            emojiButton = itemView.findViewById(R.id.home_emoji_item);
        }
    }
}
