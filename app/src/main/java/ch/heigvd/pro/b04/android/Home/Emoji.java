package ch.heigvd.pro.b04.android.Home;

import androidx.annotation.DrawableRes;

import ch.heigvd.pro.b04.android.R;

public enum Emoji {

    E0(R.drawable.emoji_0),
    E1(R.drawable.emoji_1),
    E2(R.drawable.emoji_2),
    E3(R.drawable.emoji_3),
    E4(R.drawable.emoji_4),
    E5(R.drawable.emoji_5),
    E6(R.drawable.emoji_6),
    E7(R.drawable.emoji_7),
    E8(R.drawable.emoji_8),
    E9(R.drawable.emoji_9),
    EA(R.drawable.emoji_a),
    EB(R.drawable.emoji_b),
    EC(R.drawable.emoji_c),
    ED(R.drawable.emoji_d),
    EE(R.drawable.emoji_e),
    EF(R.drawable.emoji_f);

    @DrawableRes
    private int drawable;

    /* private */ Emoji(@DrawableRes int resource) {
        this.drawable = resource;
    }

    @DrawableRes
    public int getEmoji() {
        return this.drawable;
    }

    public char getHex() {
        return (char) ('0' + (this.ordinal()));
    }
}
