package com.woodamax.stm32freshview;

/**
 * Created by maxim on 15.05.2017.
 * Needed for error handling and maybe other stuff
 */

public class BackgroundWorkerHelper {
    /**
     * CODE = 1 used for checkNewArticles() to not display the progressdialog in MainActivity
     * CODE = 2 used for sendToServer() to not display the progressdialog in LoginActivityAsync
     */
    int code;
    int count;
    int error;

    public int getCode() { return code; }

    public void setCode(int code) { this.code = code; }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
