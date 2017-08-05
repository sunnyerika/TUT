package com.woodamax.stm32freshview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class TutStartScreen extends AppCompatActivity {
    EditText tutTitle, tutText, tut_id;
    //Button RegisterStudent, ShowStudents;
    Button createTutBttn, readTutBttn;
    String tutTitleHolder, tutTextHolder, tut_idHolder;
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    String HttpURL = "http://10.0.2.2/TUTcreate.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tut_start_screen);

        tutTitle = (EditText)findViewById(R.id.editTitle);
        tutText = (EditText)findViewById(R.id.editText);
        tut_id = (EditText)findViewById(R.id.editTut_id);

        createTutBttn = (Button)findViewById(R.id.buttonSubmit);
        readTutBttn = (Button)findViewById(R.id.buttonShow);
        Button viewTutBttn = (Button) findViewById(R.id.viewTutBttn);
        viewTutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutStartScreen.this,ShowSingleRecordActivity.class);
                intent.putExtra("id", "15");
                startActivity(intent);
                finish();
            }
        });

        createTutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();
                if(CheckEditText){
                    createTut(tutTitleHolder, tutTextHolder, tut_idHolder);
                }
                else {
                    Toast.makeText(TutStartScreen.this, "Please fill all form fields.", Toast.LENGTH_LONG).show();
                }
            }
        });

        readTutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ShowAllTutsActivity.class);
                startActivity(intent);
            }
        });

    }

    public void createTut(final String t_title, final String t_text, final String t_img_id){
        class CreateTutClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(TutStartScreen.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(TutStartScreen.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("tutTitle",params[0]);
                hashMap.put("tutText",params[1]);
                hashMap.put("tutImg_id",params[2]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        CreateTutClass createTutClass = new CreateTutClass();
        createTutClass.execute(t_title,t_text,t_img_id);
    }

    public void CheckEditTextIsEmptyOrNot(){
        tutTitleHolder = tutTitle.getText().toString();
        tutTextHolder = tutText.getText().toString();
        tut_idHolder = tut_id.getText().toString();

        if(TextUtils.isEmpty(tutTitleHolder) || TextUtils.isEmpty(tutTextHolder) || TextUtils.isEmpty(tut_idHolder))
        {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }
    }
}
