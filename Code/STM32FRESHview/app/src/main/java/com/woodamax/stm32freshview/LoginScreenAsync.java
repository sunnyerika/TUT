package com.woodamax.stm32freshview;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreenAsync extends AppCompatActivity {

    EditText username;
    EditText password;
    Button submit, back;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen_async);
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
        username = (EditText) findViewById(R.id.login_user_username_async);
        password = (EditText) findViewById(R.id.login_user_password_async);
        submit = (Button) findViewById(R.id.my_login_submit_button_async);
        back = (Button) findViewById(R.id.my_login_back_button_async);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Thies method creates a new instance of the BackgroundWorker to make an Async task which handles the DB connection
    public void sendToServer(final String username, final String password){
        MainActivity.bwh.setCode(2);
        String type = "Login";
        BackgroundWorker backgroundworker = new BackgroundWorker(this);
        backgroundworker.execute(type,username,password);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_menue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        password = (EditText) findViewById(R.id.login_user_password_async);
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
