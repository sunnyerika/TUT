package com.woodamax.stm32freshview;

/**
 * Created by maxim on 03.05.2017.
 */

public class FragmentHelper {
    String Top="";
    String Center="";
    String Bottom="";

    int questionnumber;
    boolean author=false;
    int controllerType;

    public int getControllerType() {
        return controllerType;
    }

    public void setControllerType(int controllerType) {
        this.controllerType = controllerType;
    }

    public boolean isAuthor() {
        return author;
    }

    public void setAuthor(boolean author) {
        this.author = author;
    }

    public String getTop() {
        return Top;
    }

    public void setTop(String top) {
        Top = top;
    }

    public String getCenter() {
        return Center;
    }

    public void setCenter(String center) {
        Center = center;
    }

    public String getBottom() {
        return Bottom;
    }

    public void setBottom(String bottom) {
        Bottom = bottom;
    }

    public int getQuestionnumber() {return questionnumber;    }

    public void setQuestionnumber(int questionnumber) {this.questionnumber = questionnumber;    }
}
