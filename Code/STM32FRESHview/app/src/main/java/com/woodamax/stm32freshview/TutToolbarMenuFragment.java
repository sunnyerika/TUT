package com.woodamax.stm32freshview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

//CHANGE MENU REFERENCES

public class TutToolbarMenuFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "message";
    String messageText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        messageText = intent.getStringExtra(EXTRA_MESSAGE);
        View view=inflater.inflate(R.layout.fragment_tut_toolbar_menu, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tut_toolbar_frag);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        //Rewrite the title
        toolbar.setTitle("");
        //change the background color
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if(!(MainActivity.fh.getCenter().equals("Question_Fragment"))){
            Log.d("Toolbar Fragment","Displaying the back arrow");
            if (activity.getSupportActionBar() != null){
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }
        if(MainActivity.fh.getCenter().equals("Question_Fragment")){
            toolbar.setTitle("Finishing registration");
        }
        return view;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        Log.d("Toolbar Fragment","Fragment On Create Options Menu");
        if(!(MainActivity.fh.isAuthor())){
            menu.findItem(R.id.reading_edit).setVisible(false);
            Log.d("Toolbar Fragment","Is Not Author");
            Log.d("Toolbar Fragment", MainActivity.fh.getCenter());
            if(MainActivity.fh.getCenter().equals("Question_Fragment")){
                Log.d("Toolbar Fragment","Fragment QuestionFragment1 active");
                menu.findItem(R.id.reading_file).setVisible(false);
                menu.findItem(R.id.reading_edit).setVisible(false);
                menu.findItem(R.id.reading_edit).setVisible(false);
                menu.findItem(R.id.reading_create).setVisible(false);
                menu.findItem(R.id.reading_feedback).setVisible(false);
                menu.findItem(R.id.tut).setVisible(false);
                return;
            }
        }else{
            menu.findItem(R.id.reading_edit).setVisible(true);
            menu.findItem(R.id.reading_create).setVisible(true);
            menu.findItem(R.id.reading_feedback).setVisible(false);
            menu.findItem(R.id.tut).setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tut:
                Intent intent = new Intent(getActivity(), TutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}
