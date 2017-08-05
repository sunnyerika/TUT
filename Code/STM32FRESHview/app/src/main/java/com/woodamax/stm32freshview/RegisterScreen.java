package com.woodamax.stm32freshview;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

//TODO implement the show password function like the LoginScreenAsync
public class RegisterScreen extends AppCompatActivity {

    Switch myswitch;
    Button mysubmitbutton,mybackbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_register_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        //Rewrite the title
        toolbar.setTitle("");
        //change the background color
        toolbar.setBackgroundColor(Color.parseColor("#4CAF50"));
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //solution for making the submitbutton clickable only if ACCEPT AGB is set
        myswitch = (Switch) findViewById(R.id.register_switch);
        mysubmitbutton = (Button) findViewById(R.id.register_submit_button);
        mybackbutton = (Button) findViewById(R.id.register_back_button);
        // setting the button by default to GRAY
        mysubmitbutton.setTextColor(Color.GRAY);
        mysubmitbutton.setClickable(false);
        myswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myswitch.isChecked()){
                    //if the switch is checked the button is clackable an color turned into BLACK
                    mysubmitbutton.setTextColor(Color.BLACK);
                    mysubmitbutton.setClickable(true);
                    SubmitClickListener clickListener = new SubmitClickListener();
                    mysubmitbutton.setOnClickListener(clickListener);
                }else{
                    mysubmitbutton.setTextColor(Color.GRAY);
                    mysubmitbutton.setClickable(false);
                }
            }
        });
        mybackbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //To change the color in the action bar we need to define the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_register_toolbar);
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        if (item.getItemId() == R.id.register_menu_info){
            Log.e("Register Activity","Clicked on info");
        }
        return super.onOptionsItemSelected(item);
    }
}
