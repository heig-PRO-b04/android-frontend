package ch.heigvd.pro.b04.android.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.heigvd.pro.b04.android.Poll.PollActivity;
import ch.heigvd.pro.b04.android.R;

public class Home extends AppCompatActivity {
    private HomeViewModel state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        state = new ViewModelProvider(this).get(HomeViewModel.class);

        RecyclerView emojiGrid = findViewById(R.id.home_emoji_view);
        GridLayoutManager manager = new GridLayoutManager(this, 4);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 4 : 1;
            }
        });

        EmojiAdapter emojiGridAdapter = new EmojiAdapter(state, this);
        emojiGrid.setAdapter(emojiGridAdapter);
        emojiGrid.setLayoutManager(manager);

        state.getRegistrationCode().observe(this, code -> {
            Toast.makeText(this, "Code " + code, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, PollActivity.class);
            // TODO : vérifier validité et passer les informations relatives au poll
            startActivity(intent);
        });
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
