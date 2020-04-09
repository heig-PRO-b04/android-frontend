package ch.heigvd.pro.b04.android.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.emoji.widget.EmojiTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import java.util.List;

import ch.heigvd.pro.b04.android.R;

public class Home extends AppCompatActivity {
    private HomeViewModel state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        state = new ViewModelProvider(this).get(HomeViewModel.class);

        RecyclerView emojiGrid = findViewById(R.id.home_emoji_view);
        EmojiAdapter emojiGridAdapter = new EmojiAdapter(state);
        emojiGrid.setAdapter(emojiGridAdapter);
        emojiGrid.setLayoutManager(new GridLayoutManager(emojiGrid.getContext(), 4));

        EmojiTextView emojiCodeView = findViewById(R.id.home_emoji_code);

        Button clearButton = findViewById(R.id.home_emoji_code_clear);
        clearButton.setOnClickListener(v -> state.clearAll());

        state.getCodeEmoji().observe(this, new Observer<List<Emoji>>() {
            @Override
            public void onChanged(List<Emoji> emojis) {
                CharSequence txt = "";

                for (Emoji e : emojis)
                    txt = TextUtils.concat(txt, e.getEmoji());

                emojiCodeView.setText(txt);
            }
        });
    }

    /**
     * Called when the user taps the Send button
     * @param view
     */
    public void scanQR(View view) {
        //TODO voir comment récupérer le code QR et le passer au serveur ou je sais pas quoi
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }

    public void clearEmojiCode(View view) {

    }
}
