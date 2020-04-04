package ch.heigvd.pro.b04.android.Home;

import android.app.Application;

import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;

public class Polls extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        BundledEmojiCompatConfig config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);
    }
}
