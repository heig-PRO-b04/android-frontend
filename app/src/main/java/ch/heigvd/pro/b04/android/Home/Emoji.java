package ch.heigvd.pro.b04.android.Home;

public class Emoji {
    private final static Emoji elements[] = {
            new Emoji("\u2705", '0'),
            new Emoji("\uD83C\uDF7A", '1'),
            new Emoji("\uD83C\uDF54", '2'),
            new Emoji("\uD83D\uDE3B", '3'),
            new Emoji("\uD83D\uDC7B", '4'),
            new Emoji("\uD83E\uDD84", '5'),
            new Emoji("\uD83C\uDF40", '6'),
            new Emoji("\u26C4", '7'),
            new Emoji("\uD83D\uDD25", '8'),
            new Emoji("\uD83E\uDD73", '9'),
            new Emoji("\uD83E\uDD51", 'A'),
            new Emoji("\uD83E\uDD76", 'B'),
            new Emoji("\uD83C\uDF8B", 'C'),
            new Emoji("\uD83C\uDF08", 'D'),
            new Emoji("\u2614", 'E'),
            new Emoji("\uD83C\uDFB9", 'F'),
    };

    private CharSequence emoji;
    private char hex;

    public Emoji(CharSequence emoji, char hex) {
        this.emoji = emoji;
        this.hex = hex;
    }

    public CharSequence getEmoji() {
        return emoji;
    }

    public char getHex() {
        return hex;
    }

    public static Emoji at(int pos) {
        return elements[pos];
    }

    public static int size() {
        return elements.length;
    }
}
