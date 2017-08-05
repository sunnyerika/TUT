package com.woodamax.stm32freshview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
/**
 * Created by maxim on 07.04.2017.
 * No longer needed since its used with the new Activity LoginScreenAsync which uses an Async Task als BackgroundTask
 * This is better than using a Thread
 */
//TODO can be deleted due to a better performance of LoginScreenAsync
public class LoginScreen extends AppCompatActivity {

    EditText username;
    EditText password;
    Button submit;
    Toast toast;

    //change when switching the server
    final String scripturlstring = "http://m4xwe11o.ddns.net/MAD-Test/db_query_script.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_login_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        //Rewrite the title
        toolbar.setTitle("");
        //change the background color
        toolbar.setBackgroundColor(Color.parseColor("#F44336"));
        setSupportActionBar(toolbar);
        
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //define the variables
        username = (EditText) findViewById(R.id.login_user_username);
        password = (EditText) findViewById(R.id.login_user_password);
        submit = (Button) findViewById(R.id.my_login_submit_button);

        //this is the clicklisterner for the submitbutton
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(internetAvailable()){
                    if( username.getText().toString().matches("") || password.getText().toString().matches("")){
                        //call the function to send data to the server
                        makeUserToast("No Username or Password entered");
                    }else{
                        sendToServer(username.getText().toString(),password.getText().toString());
                    }
                }else{
                    //either v.getContext() or getApplicationContext
                    makeUserToast("Check Internet connectivity");
                }
            }
        });

    }

    //Method to send the entered text to server
    public void sendToServer(final String text, final String text2){

        //it should run in the background because there's happening so many things
        //when using a thread, the sendToServer method need final declaration
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    username = (EditText) findViewById(R.id.login_user_username);
                    //text which has to be transmitted to the server with specified parameters
                    //certain types must match to the php script
                    //encode it to UTF-8
                    String textparam = "text1=" + URLEncoder.encode(text, "UTF-8") + "&text2=" +URLEncoder.encode(text2, "UTF-8");

                    //now build the url
                    URL scripturl = new URL(scripturlstring);
                    //generate an Http connection to server / better performance
                    HttpURLConnection connection = (HttpURLConnection) scripturl.openConnection();
                    //We have to send data
                    connection.setDoOutput(true);
                    //set the connection properties
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //tel the function how many bytes the text has
                    connection.setFixedLengthStreamingMode(textparam.getBytes().length);

                    //used to send data and connect it with the connection
                    OutputStreamWriter contentwriter = new OutputStreamWriter(connection.getOutputStream());
                    contentwriter.write(textparam);
                    contentwriter.flush();
                    contentwriter.close();

                    //the answer stream is readble and is checked below if user exists ij DB an password is correct
                    InputStream answerstream = connection.getInputStream();
                    final String answer = getTextFromInputStream(answerstream);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //if the user exists and the password is correct than the string OK is added to the answer by the server
                            //enable Toast with answer messages only for debugging
                            if(answer.contains("Locked")){
                                //Toast.makeText(getApplicationContext(),answer,Toast.LENGTH_SHORT).show();
                                makeUserToast("Sorry, user is locked");
                            }
                            if(answer.contains("OK")){
                                //Toast.makeText(getApplicationContext(),answer,Toast.LENGTH_SHORT).show();
                                makeUserToast("Username and Password correct");
                                if(answer.contains("Yes")){
                                    //makeUserToast("Allowed to Login");
                                    Intent intent = new Intent (getApplicationContext(), ArticleScreen.class);
                                    intent.putExtra(ArticleScreen.EXTRA_MESSAGE,username.getText().toString());
                                    startActivity(intent);
                                }else {
                                    makeUserToast("Not allowed to Login yet!");
                                }
                            }else{
                                //Toast.makeText(getApplicationContext(),answer,Toast.LENGTH_SHORT).show();
                                makeUserToast("Username or Password wrong");
                            }
                        }
                    });
                    answerstream.close();
                    connection.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    //function to read the servers answer
    public String getTextFromInputStream(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader((is)));
        StringBuilder stringbuilder = new StringBuilder();
        String actualline;
        try {
            while ((actualline = reader.readLine()) != null ){
                stringbuilder.append(actualline);
                stringbuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringbuilder.toString().trim();
    }

    public void makeUserToast(String message){
        toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,200);
        toast.show();
    }

    // submit is only available when connectivity is here or if its here soon
    public boolean internetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        //get info from active network
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        //both have to be true to return true
        return networkinfo != null && networkinfo.isConnectedOrConnecting();
    }
    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_menue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        password = (EditText) findViewById(R.id.login_user_password);
        //To change the color in the action bar we need to define the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_login_toolbar);
        Drawable myDrawableOn = getResources().getDrawable(R.drawable.ic_visibility_white_24dp);
        Drawable myDrawableOff = getResources().getDrawable(R.drawable.ic_visibility_off_white_24dp);
        //Need to be defined to change the visibilty icon when clicked on "show password"
        MenuItem eyeList = toolbar.getMenu().findItem(R.id.login_menu_show_password);
        MenuItem textList = toolbar.getMenu().findItem(R.id.login_menu_show_password_text);
        //Handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        if (item.getItemId() == R.id.login_menu_show_password || item.getItemId() == R.id.login_menu_show_password_text){
            if(password.getTransformationMethod()==null){
                eyeList.setIcon(myDrawableOff);
                eyeList.setTitle(R.string.show_password);
                textList.setTitle(R.string.show_password);
                password.setTransformationMethod(new PasswordTransformationMethod());
            }else{
                eyeList.setIcon(myDrawableOn);
                eyeList.setTitle(R.string.hide_password);
                textList.setTitle(R.string.hide_password);
                password.setTransformationMethod(null);
            }
        }

        //Just something for customazation
        int id = item.getItemId();
        switch (id){
            case R.id.menu_blue:
                if(item.isChecked()){item.setChecked(false);}else{item.setChecked(true);}
                toolbar.setBackgroundColor(Color.parseColor("#2196F3"));
                return true;
            case R.id.menu_red:
                if(item.isChecked()){item.setChecked(false);}else{item.setChecked(true);}
                toolbar.setBackgroundColor(Color.parseColor("#F44336"));
                return true;
            case R.id.menu_green:
                if(item.isChecked()){item.setChecked(false);}else{item.setChecked(true);}
                toolbar.setBackgroundColor(Color.parseColor("#4CAF50"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
