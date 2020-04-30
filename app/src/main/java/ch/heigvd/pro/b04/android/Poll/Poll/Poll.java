package ch.heigvd.pro.b04.android.Poll.Poll;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.pro.b04.android.Datamodel.Question;

public class Poll {
    private String id;
    private String title;
    private String idModerator;
    private List<Question> questions;

    public Poll(String id, String idModerator, String title) {
        this.id = id;
        this.idModerator = idModerator;
        this.title = title;
        this.questions = new LinkedList<>();
    }

    public Poll(String id, String idModerator, List<Question> questions) {
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
