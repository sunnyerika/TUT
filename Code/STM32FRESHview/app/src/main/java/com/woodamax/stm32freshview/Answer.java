package com.woodamax.stm32freshview;

/**
 * Created by maxim on 14.05.2017.
 */

public class Answer {
    int ID;
    String question;

    public Answer(int ID, String question) {
        this.ID = ID;
        this.question = question;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
