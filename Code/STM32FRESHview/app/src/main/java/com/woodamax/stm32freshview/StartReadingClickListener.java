package com.woodamax.stm32freshview;

import android.content.Intent;
import android.view.View;

/**
 * Created by maxim on 02.04.2017.
 */

public class StartReadingClickListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(),ArticleScreen.class);
        view.getContext().startActivity(intent);
    }
}
