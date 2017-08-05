package com.woodamax.stm32freshview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxim on 14.05.2017.
 * Used to create the questions after the user is registered
 */

public class Questions {
    int ID;
    List<String> questions = new ArrayList<>();
    List<String> answers = new ArrayList<>();

    public List<String> getQuestion() {
        return questions;
    }

    public void setQuestion(List<String> question) {
        this.questions = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> question) {
        this.answers = question;
    }


    public int getID() {

        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


}
