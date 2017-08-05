package com.woodamax.stm32freshview;

import android.content.Intent;
import android.view.View;

/**
 * Created by maxim on 24.03.2017.
 */

public class RegisterClickListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "Clicked on login", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(),RegisterScreen.class);
        view.getContext().startActivity(intent);
    }
}
