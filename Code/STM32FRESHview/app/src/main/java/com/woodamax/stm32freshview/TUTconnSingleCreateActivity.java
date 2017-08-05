package com.woodamax.stm32freshview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class TUTconnSingleCreateActivity extends AppCompatActivity {

    String mode = "edit";
    String articleID;
    int articleID_int;

    HttpParse httpParse = new HttpParse();

    String HttpUrlTUTStable = "http://10.0.2.2/TUTcreateConnTUTStable.php";
    String HttpUrlIMAGEStable = "http://10.0.2.2/TUTcreateConnIMAGEStable.php";

    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();//for IMAGES table

    String parseResult;
    HashMap<String, String> resultHash = new HashMap<>();

    String FinalJSonObject;

    EditText title, text;
    String tutTitleHolder;

    Activity context = this;

    String TempItem;

    ProgressDialog progressDialog;
    ProgressDialog progressDialog1;

    String createdUrl;
    String createdImgText;
    String createdTitle;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutconn_single_create);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tut_connSACtoolbar);
        toolbar.setTitle("Tutorial");
        setSupportActionBar(toolbar);

        //up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_bottom_articles:
                        //textFavorites.setVisibility(View.VISIBLE);
                        //textSchedules.setVisibility(View.GONE);
                        //textMusic.setVisibility(View.GONE);
                        //Toast.makeText(TUTconnSingleActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),ArticleScreen.class);
                        startActivity(intent);
                        break;
                    case R.id.action_bottom_home:
                        //textFavorites.setVisibility(View.GONE);
                        //textSchedules.setVisibility(View.VISIBLE);
                        //textMusic.setVisibility(View.GONE);
                        Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.action_bottom_mail:

                        break;
                }
                    return false;
            }
        });

        articleID = getIntent().getStringExtra("article_id");
        articleID_int = Integer.parseInt(articleID);

        title = (EditText) findViewById(R.id.createTitle);
        tutTitleHolder = getIntent().getStringExtra("title");
        title.setText(tutTitleHolder);

        TempItem = articleID;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_sa_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                getDataFromEditTextTitle();
                getDataFromEditTextImages();
                tutCreateIMAGEStable(TempItem, createdImgText, createdUrl);
                //tutCreateIMAGEStable( transValues.get(0), transValues.get(1), transValues.get(2));
                tutCreateTUTStable(TempItem, createdTitle);//articleID is put into tut_id in TUTS
                return true;

            case R.id.action_view://view button clicked
                Intent intent = new Intent(getApplicationContext(), TUTconnSingleActivity.class);
                intent.putExtra("article_id", TempItem);
                startActivity(intent);

            default:
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void getDataFromEditTextTitle(){
        createdTitle = ((EditText) findViewById(R.id.createTitle)).getText().toString();
    }

    public void getDataFromEditTextImages(){
        createdUrl = ((EditText) findViewById(R.id.createUrl)).getText().toString();
        createdImgText = ((EditText) findViewById(R.id.createImgTxt)).getText().toString();
    }

    public void tutCreateIMAGEStable(final String tempItem, final String text, final String url){
        class TutCreateClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(TUTconnSingleCreateActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(TUTconnSingleCreateActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                resultHash.put("tut_id",params[0]);//TempItem to be put into tut_id
                resultHash.put("imgText",params[1]);
                resultHash.put("imgUrl",params[2]);
                parseResult = httpParse.postRequest(resultHash, HttpUrlIMAGEStable);
                return parseResult;
            }
        }
        TutCreateClass tutCreateClass = new TutCreateClass();
        tutCreateClass.execute(TempItem, createdImgText, createdUrl);
    }

    public void tutCreateTUTStable(String TempItem, String title){
        class TutCreateClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog1 = ProgressDialog.show(TUTconnSingleCreateActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog1.dismiss();
                Toast.makeText(TUTconnSingleCreateActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("tut_id",params[0]);//Tempitem to be put into tut_id
                hashMap.put("title",params[1]);
                finalResult = httpParse.postRequest(hashMap, HttpUrlTUTStable);
                return finalResult;
            }
        }
        TutCreateClass tutCreateClass = new TutCreateClass();
        tutCreateClass.execute(TempItem, title);
    }
}

