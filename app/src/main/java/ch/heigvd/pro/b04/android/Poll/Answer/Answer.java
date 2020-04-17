package ch.heigvd.pro.b04.android.Poll.Answer;

public class Answer {
    private String answer;
    private boolean selected;
    private int id;

    public Answer(int id, String answer) {
        this.selected = false;
        this.id = id;
        this.answer = answer;
    }

    public boolean isSelected() {
        return selected;
    }

    public void select() {
        this.selected = true;
    }

    public void deselect() {
        this.selected = false;
    }

    public Integer getId() {
        return id;
    }
}
