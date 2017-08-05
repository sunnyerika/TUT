package com.woodamax.stm32freshview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;

import com.woodamax.stm32freshview.SharedPreference;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by school_erika on 7/28/2017.
 */

public class SliderPagerAdapter  extends PagerAdapter  {
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<String> image_arraylist;
    int [] image_arraylistInt;
    String [] textArray;
    ArrayList<String> image_textlist;
    ArrayList<String> idi_array;
    String idi;
    String mode;
    View view;
    ImageView im_slider;
    private String PARCEL_KEY = "data";
    ArrayList<View> views;
    Context context;

    EditText imgUrl_edit;
    EditText imgTxt_edit;
    EditText idi_edit;
    String url_holder;
    String idi_holder;
    String imgTxt_holder;


    /*    private Context mContext;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }*/
/*
    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }
*/


    public SliderPagerAdapter(Activity activity, ArrayList<String> image_arraylist) {
        this.activity = activity;//context
        this.image_arraylist = image_arraylist;
    }
    public SliderPagerAdapter(Activity activity, ArrayList<String> image_arraylist, String mode) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        this.mode = mode;
    }

    public SliderPagerAdapter(Activity activity,  int [] image_arraylistInt) {
        this.activity = activity;
        this.image_arraylistInt = image_arraylistInt;
    }

    public SliderPagerAdapter(Activity activity,  int [] image_arraylistInt, String[] text) {
        this.activity = activity;
        this.image_arraylistInt = image_arraylistInt;
        this.textArray = text;
    }

    public SliderPagerAdapter(Activity activity,  ArrayList<String> image_arraylist, ArrayList<String> image_textlist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        this.image_textlist = image_textlist;
    }

    public SliderPagerAdapter(Activity activity,  ArrayList<String> image_arraylist, ArrayList<String> image_textlist, String mode) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        this.image_textlist = image_textlist;
        this.mode = mode;
    }

    public SliderPagerAdapter(Activity activity,  ArrayList<String> image_arraylist, ArrayList<String> image_textlist, ArrayList<String> idi_array, String mode) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        this.image_textlist = image_textlist;
        this.idi_array = idi_array;
        this.mode = mode;
    }




    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SharedPreference sharedPreference = new SharedPreference();;
        Context context = activity.getApplicationContext();

        switch (mode) {
            case "read":
            view = layoutInflater.inflate(R.layout.viewpager_item, container, false);
            im_slider = (ImageView) view.findViewById(R.id.im_slider);
            Picasso.with(activity.getApplicationContext())
                    .load(image_arraylist.get(position))
                    //.load(image_arraylistInt[position])
                    .placeholder(R.mipmap.ic_launcher) // optional
                    .error(R.mipmap.ic_launcher)         // optional
                    .into(im_slider);

            TextView txt_slider = (TextView) view.findViewById(R.id.txt_slider);
            txt_slider.setText(image_textlist.get(position));
            container.addView(view);
            break;

            case "edit":
                view = layoutInflater.inflate(R.layout.viewpager_item_edit, container, false);

                im_slider = (ImageView) view.findViewById(R.id.im_slider);
                Picasso.with(activity.getApplicationContext())
                        .load(image_arraylist.get(position))
                        //.load(image_arraylistInt[position])
                        .placeholder(R.mipmap.ic_launcher) // optional
                        .error(R.mipmap.ic_launcher)         // optional
                        .into(im_slider);

/*
                Intent intent = new Intent(context, TUTconnSingleEditActivity.class);
                intent.putExtra("data", "hello");
                activity.getApplicationContext().startActivity(intent);
                */

                //TextView idi_edit = (TextView) view.findViewById(R.id.idi_SPA);
                //idi_edit.setText(idi_array.get(position));
                //idi = idi_array.get(position);
                //txt_slider_edit = (EditText) view.findViewById(R.id.txt_slider_editSPA);
                //txt_slider_edit.setText(image_textlist.get(position));
                sharedPreference.clearSharedPreference(context);
                sharedPreference.save(context, idi);

//send url to activity
                imgUrl_edit = (EditText) view.findViewById(R.id.imgUrl_editSPA);
                url_holder = image_arraylist.get(position);
                imgUrl_edit.setText(image_arraylist.get(position));
                imgUrl_edit.addTextChangedListener(new TextWatcher(){
                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        View parentActivity = activity.findViewById(R.id.containerActivity);
                        TextView activityTextView = (TextView) parentActivity.findViewById(R.id.activityTextViewUrl);
                        if (s.length()>0){
                            activityTextView.setText(s.toString());
                        } else {
                            activityTextView.setText("default");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }});

//send idi to Activity
                idi_edit = (EditText) view.findViewById(R.id.idi_editSPA);
                idi_holder = idi_array.get(position);
                idi_edit.setText(idi_array.get(position));
                idi_edit.addTextChangedListener(new TextWatcher(){

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        View parentActivity = activity.findViewById(R.id.containerActivity);
                        TextView activityTextView = (TextView) parentActivity.findViewById(R.id.activityTextViewIdi);
                        //activityTextView.setVisibility(View.INVISIBLE);
                        if (s.length()>0){
                            activityTextView.setText(s.toString());
                        } else {
                            activityTextView.setText("default");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub

                    }});

//send ImgText to activity
                imgTxt_edit = (EditText) view.findViewById(R.id.txt_slider_editSPA);
                imgTxt_holder = image_textlist.get(position);
                imgTxt_edit.setText(image_textlist.get(position));
                imgTxt_edit.addTextChangedListener(new TextWatcher(){

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        View parentActivity = activity.findViewById(R.id.containerActivity);
                        TextView activityTextView = (TextView) parentActivity.findViewById(R.id.activityTextViewImgText);
                        if (s.length()>0){
                            activityTextView.setText(s.toString());
                        } else {
                            activityTextView.setText("default");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub

                    }});

                container.addView(view);
                break;


            default:
                return false;
        }
        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
        //return image_arraylistInt.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
         return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
