package ch.heigvd.pro.b04.android.Home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Poll.PollActivity;

import static ch.heigvd.pro.b04.android.R.id;
import static ch.heigvd.pro.b04.android.R.layout;

public class Home extends AppCompatActivity {
    private static final int COLUMN_NBR = 4;
    private HomeViewModel state;
    private NavigateToPollViewModel navigate;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_home);

        state = new ViewModelProvider(this).get(HomeViewModel.class);
        navigate = new ViewModelProvider(this).get(NavigateToPollViewModel.class);

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

        navigate.displayedPoll().observe(this, navigationInfo -> {

            // The different interesting fields.
            String token = navigationInfo.first;
            Poll poll = navigationInfo.second;

            // Build out navigation intent.
            Intent intent = new Intent(this, PollActivity.class);
            intent.putExtra(PollActivity.EXTRA_TOKEN, token);
            intent.putExtra(PollActivity.EXTRA_ID_POLL, poll.getIdPoll());
            intent.putExtra(PollActivity.EXTRA_ID_MODERATOR, poll.getIdModerator());

            // Start the activity.
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
     * @param view The button which called the method
     */
    public void scanQR(View view) {
        new IntentIntegrator(this)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setPrompt("Scan the QR Code associated with your Poll")
                .setBeepEnabled(false)
                .initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        String code = intentResult.getContents();
        if (code == null) {
            Toast.makeText(this, "QR code failed", Toast.LENGTH_LONG).show();
        } else {
            state.sendConnectRequest(code);
        }
    }
}
