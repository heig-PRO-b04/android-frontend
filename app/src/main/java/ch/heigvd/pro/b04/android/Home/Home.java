package ch.heigvd.pro.b04.android.Home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.Poll.PollActivity;
import ch.heigvd.pro.b04.android.Utils.Exceptions.TokenNotSetException;
import ch.heigvd.pro.b04.android.Utils.Persistent;

import static ch.heigvd.pro.b04.android.R.id;
import static ch.heigvd.pro.b04.android.R.layout;

public class Home extends AppCompatActivity {
    private static final int COLUMN_NBR = 4;
    private HomeViewModel state;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_home);

        state = new ViewModelProvider(this).get(HomeViewModel.class);

        try {
            String token = Persistent.getStoredTokenOrError(getApplicationContext());
            state.sendGetSessionRequest(token);
        } catch (TokenNotSetException e) {
            // Do nothing, this is the expected state !
        }

        // List of possible emojis
        RecyclerView emojiGrid = findViewById(id.home_emoji_grid);
        GridLayoutManager manager = new GridLayoutManager(this, COLUMN_NBR);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? COLUMN_NBR : 1;
            }
        });

        EmojiGridAdapter emojiAdapter = new EmojiGridAdapter(state, this);
        emojiGrid.setAdapter(emojiAdapter);
        emojiGrid.setLayoutManager(manager);

        state.getPollInfo().observe(this, poll -> {
                    Intent intent = new Intent(this, PollActivity.class);
                    intent.putExtra("idPoll", poll.get(0));
                    intent.putExtra("idModerator", poll.get(1));
                    startActivity(intent);
            });
          
        // Code of selected emojis
        CardView emojiCardView = findViewById(id.home_emoji_code_card_view);

        RecyclerView emojiCode = findViewById(id.home_emoji_code);
        GridLayoutManager emojiCodeLayout = new GridLayoutManager(this, COLUMN_NBR);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? COLUMN_NBR : 1;
            }
        });

        state.getCodeColor().observe(this, color -> emojiCardView.setCardBackgroundColor(color));

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
