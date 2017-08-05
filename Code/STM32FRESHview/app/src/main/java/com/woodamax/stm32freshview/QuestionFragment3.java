package com.woodamax.stm32freshview;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by maxim on 09.05.2017.
 */

public class QuestionFragment3 extends Fragment {
    DatabaseHelper myDBH;

    TextView questiontitle;
    TextView questiontext;
    CheckBox answer1;
    CheckBox answer2;
    CheckBox answer3;
    CheckBox answer4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_fragment, container, false);
        questiontitle = (TextView) view.findViewById(R.id.question_title);
        questiontext = (TextView) view.findViewById(R.id.question_text);
        answer1 = (CheckBox) view.findViewById(R.id.question_answer1);
        answer2 = (CheckBox) view.findViewById(R.id.question_answer2);
        answer3 = (CheckBox) view.findViewById(R.id.question_answer3);
        answer4 = (CheckBox) view.findViewById(R.id.question_answer4);

        buildQuestion(questiontitle, questiontext, answer1,answer2, answer3, answer4);

        return view;
    }

    private void buildQuestion(TextView questiontitle, TextView questiontext, CheckBox answer1, CheckBox answer2, CheckBox answer3, CheckBox answer4) {
        myDBH = new DatabaseHelper(getActivity());
        Cursor questions = myDBH.getQuestions();
        questiontitle.setText("Question 3");
        questiontext.setText(MainActivity.qh.questions.get(2));
        answer1.setText(MainActivity.qh.answers.get(2));
        answer2.setText(MainActivity.qh.answers.get(6));
        answer3.setText(MainActivity.qh.answers.get(10));
        answer4.setText(MainActivity.qh.answers.get(14));
    }
}
