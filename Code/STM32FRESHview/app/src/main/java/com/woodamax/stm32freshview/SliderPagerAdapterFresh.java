package com.woodamax.stm32freshview;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by school_erika on 8/5/2017.
 */

public class SliderPagerAdapterFresh extends PagerAdapter {

    private LayoutInflater layoutInflater;

    Activity activity;
    ArrayList <String> image_arraylist;
    ArrayList <String> image_textlist;

    View view;
    ImageView im_slider;


    public SliderPagerAdapterFresh(Activity activity, ArrayList<String> image_arraylist, ArrayList<String> image_textlist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        this.image_textlist = image_textlist;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = layoutInflater.inflate(R.layout.viewpager_item_fresh, container, false);
                im_slider = (ImageView) view.findViewById(R.id.im_slider);
                Glide.with(activity.getApplicationContext())
                        .load(image_arraylist.get(position))
                        //.load(image_arraylistInt[position])
                        //.load(R.drawable.wall)
                        .placeholder(R.mipmap.ic_launcher) // optional
                        .error(R.mipmap.ic_launcher)         // optional
                        .into(im_slider);

                TextView txt_slider = (TextView) view.findViewById(R.id.txt_slider);
                txt_slider.setText(image_textlist.get(position));
                container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
        //return 1;
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
