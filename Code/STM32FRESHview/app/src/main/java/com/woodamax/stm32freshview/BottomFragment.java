package com.woodamax.stm32freshview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by maxim on 05.04.2017.
 */
// TODO function for buttons
public class BottomFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "message";
    String messageText;
    Button back;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.article_bottom_fragment, container, false);
        Intent intent = getActivity().getIntent();
        //messageText = intent.getStringExtra(EXTRA_MESSAGE);
        back = (Button) view.findViewById(R.id.my_reading_back_button);
        back.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent (v.getContext(), MainActivity.class);
                        getActivity().startActivity(intent);
                    }
                }
        );
        Button articleButton = (Button) view.findViewById(R.id.my_reading_article_button);
        if(MainActivity.fh.getCenter().equals("Article_selection_Fragment") || MainActivity.fh.getCenter().equals("Question_Fragment")){
            articleButton.setVisibility(view.INVISIBLE);
        }

        return view;
    }
}
