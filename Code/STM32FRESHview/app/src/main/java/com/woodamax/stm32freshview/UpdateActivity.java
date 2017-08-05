package com.woodamax.stm32freshview;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class UpdateActivity extends AppCompatActivity {

    String HttpURL = "http://10.0.2.2/TUTupdateConn.php";
    ProgressDialog progressDialog;
    String finalResult ;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    EditText tutTitle, tutText, tut_id;
    Button tutUpdateBtn;
    String tutIdHolder, tutTitleHolder, tutTextHolder, tut_idHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);tutTitle = (EditText)findViewById(R.id.editTitle);
        tutText = (EditText)findViewById(R.id.editText);
        tut_id = (EditText)findViewById(R.id.editTut_id);

        tutUpdateBtn = (Button)findViewById(R.id.UpdateButton);

        tutIdHolder = getIntent().getStringExtra("article_id");
        tutTitleHolder = getIntent().getStringExtra("title");
        tutTextHolder = getIntent().getStringExtra("text");
        tut_idHolder = getIntent().getStringExtra("tut_id");

        tutTitle.setText(tutTitleHolder);
        tutText.setText(tutTextHolder);
        tut_id.setText(tut_idHolder);

        // Adding click listener to update button .
        tutUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Getting data from EditText after button click.
                GetDataFromEditText();

                tutUpdate(tutIdHolder,tutTitleHolder,tutTextHolder, tut_idHolder);
            }
        });
    }
    // Method to get existing data from EditText.
    public void GetDataFromEditText(){
        tutTitleHolder = tutTitle.getText().toString();
        tutTextHolder = tutText.getText().toString();
        tut_idHolder = tut_id.getText().toString();
    }

    public void tutUpdate(final String t_id, final String t_title, final String t_text, final String tut_id){
        class TutUpdateClass extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(UpdateActivity.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(UpdateActivity.this,httpResponseMsg.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("id",params[0]);
                hashMap.put("tutTitle",params[1]);
                hashMap.put("tutText",params[2]);
                hashMap.put("tut_id",params[3]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);
                return finalResult;
            }
        }
        TutUpdateClass tutUpdateClass = new TutUpdateClass();
        tutUpdateClass.execute(t_id,t_title,t_text,tut_id);
    }
}
