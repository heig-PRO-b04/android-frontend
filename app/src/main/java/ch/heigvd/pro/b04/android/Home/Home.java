package ch.heigvd.pro.b04.android.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.Poll.PollActivity;
import ch.heigvd.pro.b04.android.R;

public class Home extends AppCompatActivity {
    private static final int COLUMN_NBR = 4;
    private HomeViewModel state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        state = new ViewModelProvider(this).get(HomeViewModel.class);

        // List of possible emojis
        RecyclerView emojiGrid = findViewById(R.id.home_emoji_view);
        GridLayoutManager manager = new GridLayoutManager(this, COLUMN_NBR);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? COLUMN_NBR : 1;
            }
        });

        EmojiAdapter emojiAdapter = new EmojiAdapter(state, this);
        emojiGrid.setAdapter(emojiAdapter);
        emojiGrid.setLayoutManager(manager);

        state.getPollInfo().observe(this, poll -> {
                    Intent intent = new Intent(this, PollActivity.class);
                    intent.putExtra("idPoll", poll.get(0));
                    intent.putExtra("idModerator", poll.get(1));
                    intent.putExtra("token", state.getToken());
                    startActivity(intent);
            });
          
        // Code of selected emojis
        RecyclerView emojiCode = findViewById(R.id.home_emoji_code);
        GridLayoutManager emojiCodeLayout = new GridLayoutManager(this, COLUMN_NBR);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? COLUMN_NBR : 1;
            }
        });
        EmojiCodeAdapter emojiCodeAdapter = new EmojiCodeAdapter(state, this);
        emojiCode.setAdapter(emojiCodeAdapter);
        emojiCode.setLayoutManager(emojiCodeLayout);
    }

    /**
     * Called when the user taps the Send button
     *
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
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }
    }
}
