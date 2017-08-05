package com.woodamax.stm32freshview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Maximilian on 11.06.2017.
 */

public class QuestionPageAdapter extends FragmentStatePagerAdapter{
    int mNumOfTabs;
    public QuestionPageAdapter(FragmentManager fm,int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                QuestionFragment1 tab1 = new QuestionFragment1();
                MainActivity.fh.setQuestionnumber(1);
                return tab1;
            case 1:
                QuestionFragment2 tab2 = new QuestionFragment2();
                MainActivity.fh.setQuestionnumber(2);
                return tab2;
            case 2:
                QuestionFragment3 tab3 = new QuestionFragment3();
                MainActivity.fh.setQuestionnumber(3);
                return tab3;
            case 3:
                QuestionFragment4 tab4 = new QuestionFragment4();
                MainActivity.fh.setQuestionnumber(4);
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
