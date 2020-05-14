package ch.heigvd.pro.b04.android.Home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import ch.heigvd.pro.b04.android.Datamodel.Poll;
import ch.heigvd.pro.b04.android.Poll.PollActivity;
import ch.heigvd.pro.b04.android.R;

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

        setupEmojiGrid();
        setupEmojiCardView();
        setupNavigation();
    }

    private void setupEmojiGrid() {
        RecyclerView emojiGrid = findViewById(id.home_grid);
        GridLayoutManager manager = new GridLayoutManager(this, COLUMN_NBR);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? COLUMN_NBR : 1;
            }
        });

        EmojiGridAdapter emojiAdapter = new EmojiGridAdapter(state);
        emojiGrid.setAdapter(emojiAdapter);
        emojiGrid.setLayoutManager(manager);
    }

    private void setupEmojiCardView() {
        // Views
        CardView emojiCardView = findViewById(id.home_code_card_view);
        Button scanningButton = findViewById(id.button);
        state.getRequestState().observe(this, state -> {
            int color;
            switch (state) {
                case ERROR:
                    color = R.color.colorError;
                    break;
                case SENDING:
                    color = R.color.seaside_200;
                    break;
                default:
                    color = android.R.color.white;
            }

            emojiCardView.setCardBackgroundColor(ContextCompat.getColor(this, color));
        });

        // RecyclerView
        RecyclerView emojiCode = findViewById(id.home_code_recycler_view);
        GridLayoutManager emojiCodeLayout = new GridLayoutManager(this, COLUMN_NBR);
        EmojiCodeAdapter emojiCodeAdapter = new EmojiCodeAdapter(state, this);

        emojiCode.setAdapter(emojiCodeAdapter);
        emojiCode.setLayoutManager(emojiCodeLayout);

        // Button visibility
        state.getCodeEmoji().observe(this, emojis -> {
            if (emojis.isEmpty()) {
                emojiCardView.setVisibility(View.INVISIBLE);
                scanningButton.setVisibility(View.VISIBLE);
            } else {
                emojiCardView.setVisibility(View.VISIBLE);
                scanningButton.setVisibility(View.INVISIBLE);
            }
        });

        // Clear button
        ImageButton clearButton = findViewById(id.home_code_clear);
        clearButton.setOnClickListener(v -> state.clearOneEmoji());
        clearButton.setOnLongClickListener(v -> {
            state.reinitializeEmojiBuffer();
            return true;
        });
        state.getClearButtonRes().observe(this, clearButton::setImageDrawable);
    }

    private void setupNavigation() {
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
