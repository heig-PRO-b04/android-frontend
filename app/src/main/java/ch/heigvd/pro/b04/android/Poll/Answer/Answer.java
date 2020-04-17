package ch.heigvd.pro.b04.android.Poll.Answer;

public class Answer {
    private String answer;
    private int id;

    public Answer(int id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }
}
