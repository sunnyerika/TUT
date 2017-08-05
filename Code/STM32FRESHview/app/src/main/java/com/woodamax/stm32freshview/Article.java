package com.woodamax.stm32freshview;

/**
 * Created by maxim on 30.04.2017.
 */

public class Article {
    String title;
    String description;
    String articletext;
    String code;

    public Article(String title, String description, String articletext, String code) {
        this.title = title;
        this.description = description;
        this.articletext = articletext;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArticletext() {
        return articletext;
    }

    public void setArticletext(String articletext) {
        this.articletext = articletext;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
