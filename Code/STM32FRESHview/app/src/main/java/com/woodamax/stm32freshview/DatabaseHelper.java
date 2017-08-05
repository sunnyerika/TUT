package com.woodamax.stm32freshview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by maxim on 08.04.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "stm32freshview.db";

    /**
     * Article table
     *
     * ARTICLE TABLE
     * ------------------------------------------------------------------
     * |    ID  |   TITLE   |   DESCRIPTION |   PICTURE |   ARTICLETEXT |
     * ------------------------------------------------------------------
     */
    public static final String TABLE_ARTICLE = "article_table";
    public static final String ARTICLE_COL_1 = "ID";
    public static final String ARTICLE_COL_2 = "TITLE";
    public static final String ARTICLE_COL_3 = "DESCRIPTION";
    public static final String ARTICLE_COL_4 = "PICTURE";
    public static final String ARTICLE_COL_5 = "ARTICLETEXT";

    /**
     * Qestion,Answer, QuestionAnswer tables
     *
     * QUESTION TABLE               ANSWER TABLE            QUESTION/ANSWER TABLE
     * --------------------------   ---------------------   -----------------------------------------
     * |    ID  |   QUESTION    |   |   ID  |   ANSWER  |   |   ID  |   QUESTION_ID |   ANSWER_ID   |
     * --------------------------   ---------------------   -----------------------------------------
     */

    public static final String TABLE_QUESTIONS = "question_table";
    public static final String TABLE_ANSWER = "answer_table";
    public static final String TABLE_QUESTION_ANSWER = "question_answer_table";

    public static final String QUESTION_COL_1 = "ID";
    public static final String ANSWER_COL_1 = "ID";
    public static final String QUESTION_ANSWER_COL_1 = "ID";
    public static final String QUESTION_COL_2 = "QUESTION";
    public static final String ANSWER_COL_2 = "ANSWER";
    public static final String QUESTION_ANSWER_COL_2 = "QUESTION_ID";
    public static final String QUESTION_ANSWER_COL_3 = "ANSWER_ID";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_ARTICLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, DESCRIPTION TEXT, PICTURE TEXT, ARTICLETEXT TEXT)");
        db.execSQL("create table "+ TABLE_QUESTIONS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION TEXT)");
        db.execSQL("create table "+ TABLE_ANSWER + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, ANSWER TEXT)");
        db.execSQL("create table "+ TABLE_QUESTION_ANSWER + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, QUESTION_ID INTEGER, ANSWER_ID INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ARTICLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ANSWER);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_QUESTION_ANSWER);
        onCreate(db);
    }

    //This method writes into the DB
    public boolean insertArticleData(String title, String description, String picture, String articletext){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ARTICLE_COL_2,title);
        contentValues.put(ARTICLE_COL_3,description);
        contentValues.put(ARTICLE_COL_4,picture);
        contentValues.put(ARTICLE_COL_5,articletext);
        long result = db.insert(TABLE_ARTICLE,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertQuestionData(String question){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTION_COL_2,question);
        long result = db.insert(TABLE_QUESTIONS,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertAnswerData(String answer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ANSWER_COL_2,answer);
        long result = db.insert(TABLE_ANSWER,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertCorrectAnswerData(int question,int answer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTION_ANSWER_COL_2,question);
        contentValues.put(QUESTION_ANSWER_COL_3,answer);
        long result = db.insert(TABLE_QUESTION_ANSWER,null,contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getArticleDescription(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+TABLE_ARTICLE,null);
        return result;
    }

    public Cursor getQuestions(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+TABLE_QUESTIONS,null);
        return result;
    }

    public Cursor getAnswers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+TABLE_ANSWER,null);
        return result;
    }

    public Cursor getRightAnswer(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+TABLE_QUESTION_ANSWER,null);
        return result;
    }

    public boolean updateDataTitle(String id, String title){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ARTICLE_COL_1,id);
        contentValues.put(ARTICLE_COL_2,title);
        db.update(TABLE_ARTICLE,contentValues,"ID = ?", new String[] {id});
        return true;
    }

    public boolean updateArticleDataText(String id, String text){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ARTICLE_COL_1,id);
        contentValues.put(ARTICLE_COL_5,text);
        db.update(TABLE_ARTICLE,contentValues,"ID = ?", new String[] {id});
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ARTICLE, "ID = ?",new String[] {id});
    }

    public void dropTableArticle(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ARTICLE);
    }

    public void dropTableQuestions(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ANSWER);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_QUESTION_ANSWER);
    }
    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
