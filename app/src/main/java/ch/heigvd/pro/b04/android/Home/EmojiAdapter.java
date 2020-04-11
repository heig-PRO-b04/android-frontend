package ch.heigvd.pro.b04.android.Home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        ImageButton button = holder.emojiButton;
        Emoji emoji = Emoji.values()[position];

        button.setImageResource(emoji.getEmoji());
        button.setOnClickListener(v -> state.addNewEmoji(emoji));
    }

    @Override
    public int getItemCount() {
        return Emoji.values().length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton emojiButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            emojiButton = itemView.findViewById(R.id.home_emoji_item);
        }
    }
}
