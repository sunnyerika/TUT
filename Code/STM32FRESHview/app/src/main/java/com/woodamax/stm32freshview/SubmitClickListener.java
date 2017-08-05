package com.woodamax.stm32freshview;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by maxim on 25.03.2017.
 */

//TODO add the questioning screen after filling in the registration form
public class SubmitClickListener implements View.OnClickListener{
    EditText surname,firstname,address,email,password,confirmpassword;
    Toast toast;

    public void onClick(View view) {
        Toast.makeText(view.getContext(),"Good, you accepted the AGB",Toast.LENGTH_SHORT).show();
        //Need to get the parent for the clicklisterner view ergo the register view with all of its elements
        View parent = view.getRootView();
        String type = "Register";
        surname = (EditText) parent.findViewById(R.id.register_surname);
        firstname = (EditText) parent.findViewById(R.id.register_firstname);
        address = (EditText) parent.findViewById(R.id.register_address);
        email = (EditText) parent.findViewById(R.id.register_email);
        password = (EditText) parent.findViewById(R.id.register_password);
        confirmpassword = (EditText) parent.findViewById(R.id.register_password_confirm);
        //Toast.makeText(view.getContext(),surname.getText().toString()+" "+firstname.getText().toString(),Toast.LENGTH_SHORT).show();
        if(validateInput(
                surname.getText().toString(),
                firstname.getText().toString(),
                address.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                confirmpassword.getText().toString())){
            BackgroundWorker backgroundWorker = new BackgroundWorker(view.getContext());
            backgroundWorker.execute(type,
                    surname.getText().toString(),
                    firstname.getText().toString(),
                    address.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    confirmpassword.getText().toString());
            toast.makeText(view.getContext(),"Registration is complete",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(view.getContext(),QuestionScreen.class);
            view.getContext().startActivity(intent);
        }else{
            toast.makeText(view.getContext(),"Please fill in all fields!",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * If it works... It isn't stupid
     * @param var1 surname
     * @param var2 firstname
     * @param var3 address
     * @param var4 email
     * @param var5 password
     * @param var6 confirmassword
     * @return true/false wether input was correct or not
     */
    public boolean validateInput(String var1,String var2, String var3, String var4, String var5, String var6){
        if(var1 == " ") return false;
        if(var2 == " ") return false;
        if(var3 == " ") return false;
        if(!(var4.contains("@"))) return false;
        if(var5 == " ") return false;
        if(var6 == " ") return false;
        if(!(var5.equals(var6)) ) return false;
        return true;
    }
}
