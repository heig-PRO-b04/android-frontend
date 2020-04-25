package ch.heigvd.pro.b04.android.Poll.Poll;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Poll.Question.Question;

public class Poll {
    private String id;
    private String title;
    private String idModerator;
    private String status;
    private List<Question> questions;

    public Poll(String id, String idModerator, String status) {
        this.id = id;
        this.idModerator = idModerator;
        this.questions = new LinkedList<>();
    }

    public Poll(String id, String idModerator, String status, List<Question> questions) {
        this.id = id;
        this.idModerator = idModerator;
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        if (questions.contains(question))
            return;
        questions.add(question);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdModerator() {
        return idModerator;
    }
}
