package ch.heigvd.pro.b04.android.Poll.Answer;

public class Answer {
    private String answere;
    private int id;

    public Answer(int id, String answere) {
        this.id = id;
        this.answere = answere;
    }

    public Integer getId() {
        return id;
    }
}
