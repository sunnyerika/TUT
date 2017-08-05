package com.woodamax.stm32freshview;

import android.content.Intent;
import android.view.View;

/**
 * Created by maxim on 23.03.2017.
 */

public class LoginClickListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "Clicked on login", Toast.LENGTH_SHORT).show();
        MainActivity.fh.setAuthor(true);
        Intent intent = new Intent(view.getContext(), LoginScreenAsync.class);
        view.getContext().startActivity(intent);
    }
}
