package ch.heigvd.pro.b04.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String SCANN_BUTTON = "QR code";
    private static final String OK = "OK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user taps the Send button
     * @param view
     */
    public void scannQR(View view) {
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

    /**
     * Called when the user taps the Send button
     * @param view
     */
    public void ConfirmCode(View view) {
        //TODO voir comment récupérer le code et le passer au serveur ou je sais pas quoi
        Intent intent = new Intent(this, PollActivity.class);
        startActivity(intent);

    }
}
