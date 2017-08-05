package com.woodamax.stm32freshview;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.woodamax.stm32freshview.DatabaseHelper.DATABASE_NAME;
//TODO Add answers and linked table questions/answers
public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDBH;

    static Questions qh = new Questions();
    static FragmentHelper fh = new FragmentHelper();
    static BackgroundWorkerHelper bwh = new BackgroundWorkerHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        LoginClickListener clickListener = new LoginClickListener();
        RegisterClickListener clickListener1 = new RegisterClickListener();
        StartReadingClickListener clickListener2 = new StartReadingClickListener();
        Button login = (Button) findViewById(R.id.my_login_button);
        Button register = (Button) findViewById(R.id.my_sign_up_button);
        Button reading = (Button) findViewById(R.id.my_start_reading_button);
        Button tut = (Button) findViewById(R.id.tutStartScreenBttn);

        tut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(),TUTconnSingleActivity.class);
                Intent intent = new Intent(getApplicationContext(),TUTconnFreshView.class);
                intent.putExtra("article_id", "3");
                intent.putExtra("test", "3");
                startActivity(intent);
            }
        });
        login.setOnClickListener(clickListener);
        register.setOnClickListener(clickListener1);
        reading.setOnClickListener(clickListener2);
        bwh.setCode(1);
        checkForNewArticles();
        writeQuestionsInDb();
        writeAnswersInDb();
        writeQuestionAnswerInDb();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_info){
            if(!(internetAvailable())){
                Toast.makeText(MainActivity.this,"Check internet connectivity",Toast.LENGTH_SHORT).show();
                return false;
            }
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.onPreExecute();
            deleteDatabase(DATABASE_NAME);
            bwh.setCode(1);
            fetchArticlesDescription();
            bwh.setCode(1);
            fetchArticleText();


        }else if(item.getItemId() == R.id.menu_question){
            Log.d("Main Activity", "Debuging question database");
            debugQuestionDatabse();
        }else if(item.getItemId() == R.id.menu_answer){
            Log.d("Main Activity", "Debuging answer database");
            debugAnswerDatabse();
        }else{
            Log.d("Main Activity", "Debuging article database");
            debugDatabse();
        }
        return true;
    }



    public boolean internetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isConnectedOrConnecting();
    }

    /**
     * Used due to development
     */
    private void debugQuestionDatabse() {
        myDBH = new DatabaseHelper(this);
        Cursor res = myDBH.getQuestions();
        if(res.getCount() == 0) {
            showMessage("Error","Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :"+ res.getString(0)+"\n");
            buffer.append("Question :"+ res.getString(1)+"\n");
        }

        showMessage("Question table",buffer.toString());
    }

    private void debugAnswerDatabse() {
        myDBH = new DatabaseHelper(this);
        Cursor res = myDBH.getAnswers();
        if(res.getCount() == 0) {
            showMessage("Error","Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :"+ res.getString(0)+"\n");
            buffer.append("Answer :"+ res.getString(1)+"\n");
        }

        showMessage("Answer table",buffer.toString());
    }


    /**
     * Method to display the local DB
     */
    private void debugDatabse() {
        myDBH = new DatabaseHelper(this);
        Cursor res = myDBH.getArticleDescription();
        Cursor debug = myDBH.getRightAnswer();
        if(res.getCount() == 0) {
            showMessage("Error","Nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("Id :"+ res.getString(0)+"\n");
            buffer.append("Title :"+ res.getString(1)+"\n");
            buffer.append("Description :"+ res.getString(2)+"\n");
            buffer.append("Text :"+ res.getString(4)+"\n");
        }
        while (debug.moveToNext()) {
            Log.d("Id :",debug.getString(0));
            Log.d("Question :",debug.getString(1));
            Log.d("Answer :",debug.getString(2));
        }

        showMessage("Article table",buffer.toString());
    }

    /**
     * Build the alert screen to show the local DB
     * @param title alert dialog title
     * @param Message DB query result from the buffer
     */
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    /**
     * This method calls the background worker to get the articles description and title
     */
    private void fetchArticlesDescription() {
        String type = "FetchArticleDescription";
        String article = "article";
        BackgroundWorker backgroundworker = new BackgroundWorker(this);
        backgroundworker.execute(type,article);
    }

    /**
     * This method calls the background worker to get the articlestext
     */
    private void fetchArticleText() {
        String type = "FetchArticleText";
        String articletext = "articletext";
        BackgroundWorker backgroundworker = new BackgroundWorker(this);
        backgroundworker.execute(type,articletext);
    }

    /**
     * This method calls the background worker to get the number of articles
     */
    private void checkForNewArticles() {
        String type = "FetchNewArticle";
        String article = "article";
        BackgroundWorker backgroundworker = new BackgroundWorker(this);
        backgroundworker.execute(type,article);
    }

    /**
     * Used to write the questions to the DB
     */
    private void writeQuestionsInDb() {
        String type = "writeQuestionsInDb";
        bwh.setCount(4);
        BackgroundWorker backgroundworker = new BackgroundWorker(this);
        backgroundworker.doInBackground(type,
                getResources().getString(R.string.Question1),
                getResources().getString(R.string.Question2),
                getResources().getString(R.string.Question3),
                getResources().getString(R.string.Question4));
    }

    /**
     * If it works... it isn't stupid...
     */
    private void writeAnswersInDb(){
        String type = "writeAnswersInDb";
        bwh.setCount(4*4);
        BackgroundWorker backgroundworker = new BackgroundWorker(this);
        backgroundworker.doInBackground(type,
                getResources().getString(R.string.Answer1Q1),
                getResources().getString(R.string.Answer1Q2),
                getResources().getString(R.string.Answer1Q3),
                getResources().getString(R.string.Answer1Q4),
                getResources().getString(R.string.Answer2Q1),
                getResources().getString(R.string.Answer2Q2),
                getResources().getString(R.string.Answer2Q3),
                getResources().getString(R.string.Answer2Q4),
                getResources().getString(R.string.Answer3Q1),
                getResources().getString(R.string.Answer3Q2),
                getResources().getString(R.string.Answer3Q3),
                getResources().getString(R.string.Answer3Q4),
                getResources().getString(R.string.Answer4Q1),
                getResources().getString(R.string.Answer4Q2),
                getResources().getString(R.string.Answer4Q3),
                getResources().getString(R.string.Answer4Q4));
    }

    private void writeQuestionAnswerInDb() {
        String type ="writeQuestionAnswerInDb";
        bwh.setCount(4);
        BackgroundWorker backgroundworker = new BackgroundWorker(this);
        backgroundworker.doInBackground(type);
    }
}
