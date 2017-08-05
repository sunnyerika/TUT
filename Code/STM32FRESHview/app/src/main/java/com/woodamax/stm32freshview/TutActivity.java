package com.woodamax.stm32freshview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;



public class TutActivity extends AppCompatActivity {

    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tut);

        TutToolbarMenuFragment toolbar = new TutToolbarMenuFragment();
        BottomFragment bottom = new BottomFragment();

        ft.add(R.id.tut_toolbar_menu_fragment,toolbar,"Toolbar_fragment");
        ft.add(R.id.tut_act_bottomFrag, bottom, "Bottom_Fragment");
       // ft.add(R.id.article_text_container, article, "Article_selection_Fragment");
        ft.commit();

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article_menue, menu);
        //menu.findItem(R.id.reading_edit).setVisible(true);
        return true;
    }

}
