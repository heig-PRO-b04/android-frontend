package ch.heigvd.pro.b04.android.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import ch.heigvd.pro.b04.android.R;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView emojiGrid = findViewById(R.id.home_emoji_view);
        EmojiAdapter adapter = new EmojiAdapter();
        emojiGrid.setAdapter(adapter);
        emojiGrid.setLayoutManager(new GridLayoutManager(emojiGrid.getContext(), 4));
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
}
