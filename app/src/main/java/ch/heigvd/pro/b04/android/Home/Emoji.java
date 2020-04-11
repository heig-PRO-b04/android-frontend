package ch.heigvd.pro.b04.android.Home;

import androidx.annotation.DrawableRes;

import ch.heigvd.pro.b04.android.R;

public enum Emoji {

    E0(R.drawable.emoji_0, '0'),
    E1(R.drawable.emoji_1, '1'),
    E2(R.drawable.emoji_2, '2'),
    E3(R.drawable.emoji_3, '3'),
    E4(R.drawable.emoji_4, '4'),
    E5(R.drawable.emoji_5, '5'),
    E6(R.drawable.emoji_6, '6'),
    E7(R.drawable.emoji_7, '7'),
    E8(R.drawable.emoji_8, '8'),
    E9(R.drawable.emoji_9, '9'),
    EA(R.drawable.emoji_a, 'A'),
    EB(R.drawable.emoji_b, 'B'),
    EC(R.drawable.emoji_c, 'C'),
    ED(R.drawable.emoji_d, 'D'),
    EE(R.drawable.emoji_e, 'E'),
    EF(R.drawable.emoji_f, 'F');

    @DrawableRes
    private int drawable;

    private char c;

    /* private */ Emoji(@DrawableRes int resource, char c) {
        this.drawable = resource;
        this.c = c;
    }

    @DrawableRes
    public int getEmoji() {
        return this.drawable;
    }

    public char getHex() {
        return this.c;
    }
}
