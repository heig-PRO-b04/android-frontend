package ch.heigvd.pro.b04.android.Poll.Answer;

import androidx.annotation.Nullable;

public class Answer {
    private String answer;
    private boolean selected;
    private int id;

    public Answer(int id, String answer) {
        this.selected = false;
        this.id = id;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
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

    @Override
    public boolean equals(@Nullable Object obj) {
        Answer a = (Answer) obj;
        return answer.equals(a.answer) && a.id == id;
    }
}   
