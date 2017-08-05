package com.woodamax.stm32freshview;

import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
//TODO maybe use an ListAdapter to swipe from one question to the other...
public class QuestionScreen extends AppCompatActivity {

    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();

    DatabaseHelper myDBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_screen);

        MainActivity.bwh.setCode(1);
        writeQuestionsInDb();
        MainActivity.bwh.setCode(1);
        writeAnswersInDb();
        MainActivity.bwh.setCode(1);
        writeQuestionAnswerInDb();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Q1"));
        tabLayout.addTab(tabLayout.newTab().setText("Q2"));
        tabLayout.addTab(tabLayout.newTab().setText("Q3"));
        tabLayout.addTab(tabLayout.newTab().setText("Q4"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ToolbarFragment toolbar = new ToolbarFragment();
        BottomFragment bottom = new BottomFragment();

        ft.add(R.id.article_toolbar,toolbar,"Toolbar_fragment");
        ft.add(R.id.question_text_container_bottom, bottom, "Bottom_Fragment");
        //ft.add(R.id.question_text_container, question, "Question_Fragment");
        ft.commit();
        // add back arrow to toolbar
        MainActivity.fh.setTop("Toolbar_fragment");
        MainActivity.fh.setCenter("Question_Fragment");
        MainActivity.fh.setBottom("Bottom_Fragment");

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new QuestionPageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 4){
                    Log.e("QA","Tab 4");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article_menue, menu);
        //menu.findItem(R.id.reading_edit).setVisible(true);
        return true;
    }

    /**
     * Used to write the questions to the DB
     */
    private void writeQuestionsInDb() {
        String type = "writeQuestionsInDb";
        MainActivity.bwh.setCount(4);
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
        MainActivity.bwh.setCount(4*4);
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
        MainActivity.bwh.setCount(4);
        BackgroundWorker backgroundworker = new BackgroundWorker(this);
        backgroundworker.doInBackground(type);
    }
}
