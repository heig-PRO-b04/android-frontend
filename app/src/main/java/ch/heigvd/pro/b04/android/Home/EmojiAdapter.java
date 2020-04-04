package ch.heigvd.pro.b04.android.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.emoji.widget.EmojiTextView;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.R;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
    private final static Emoji elements[] = {
            new Emoji("\u2705", '0'),
            new Emoji("\uD83C\uDF7A", '1'),
            new Emoji("\uD83C\uDF54", '2'),
            new Emoji("\u2705", '3'),
            new Emoji("\uD83D\uDC7B", '4'),
            new Emoji("\uD83E\uDD84", '5'),
            new Emoji("\uD83C\uDF40", '6'),
            new Emoji("\u26C4", '7'),
            new Emoji("\uD83D\uDD25", '8'),
            new Emoji("\uD83E\uDD73", '9'),
            new Emoji("\uD83E\uDD51", 'A'),
            new Emoji("\uD83E\uDD76", 'B'),
            new Emoji("\uD83E\uDD76", 'C'),
            new Emoji("\uD83C\uDF08", 'D'),
            new Emoji("\u2614", 'E'),
            new Emoji("\u2614", 'F'),
    };

    public EmojiAdapter() {
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
        // Context holder.emojiTextView.getContext();
        holder.emojiTextView.setText(elements[position].emoji);
    }

    @Override
    public int getItemCount() {
        return elements.length;
    }

    private static class Emoji {
        private CharSequence emoji;
        private char hex;

        public Emoji(CharSequence emoji, char hex) {
            this.emoji = emoji;
            this.hex = hex;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public EmojiTextView emojiTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            emojiTextView = itemView.findViewById(R.id.home_emoji_item);
        }
    }
}
