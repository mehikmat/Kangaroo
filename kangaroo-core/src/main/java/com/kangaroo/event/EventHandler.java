package com.kangaroo.event;

import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.kangaroo.ContactViewerActivity;
import com.kangaroo.MainActivity;
import com.kangaroo.OptionActivity;
import com.kangaroo.PhoneCallerActivity;
import com.kangaroo.R;
import com.kangaroo.SignInActivity;
import com.kangaroo.SignUpActivity;
import com.kangaroo.rest.ContactDownloaderActivity;
import com.kangaroo.util.AppConstant;

public class EventHandler implements OnClickListener, OnTouchListener {
    MainActivity mainActivity;
    SignInActivity signInActivity;
    OptionActivity optionActivity;
    PhoneCallerActivity phoneCallerActivity;

    public EventHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public EventHandler(SignInActivity signInActivity) {
        this.signInActivity = signInActivity;
    }

    public EventHandler(OptionActivity optionActivity) {
        this.optionActivity = optionActivity;
    }

    public EventHandler(PhoneCallerActivity phoneCallerActivity) {
        this.phoneCallerActivity = phoneCallerActivity;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btn_signin){
            Intent signinIntent=new Intent(mainActivity,SignInActivity.class);
            mainActivity.startActivity(signinIntent);
        }

        if(view.getId() == R.id.btn_import_from_phone){
            Intent viewIntent=new Intent(optionActivity,ContactViewerActivity.class);
            optionActivity.startActivity(viewIntent);
        }

        if(view.getId()==R.id.btn_import_from_server){
            Intent addIntent=new Intent(optionActivity,ContactDownloaderActivity.class);
            optionActivity.startActivity(addIntent);

        }

        if(view.getId()==R.id.btn_signup){
            Intent signUpIntent = new Intent(mainActivity,SignUpActivity.class);
            mainActivity.startActivity(signUpIntent);
        }

        if(view.getId()==R.id.btn_phone_call){
            Intent phoneCall=new Intent(Intent.ACTION_CALL);
            phoneCall.setData(Uri.parse("tel:" + System.getProperty(AppConstant.CONTACT_NUMBER).toString()));
            phoneCallerActivity.startActivity(phoneCall);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId()==R.id.btn_signin && event.getAction()==MotionEvent.ACTION_DOWN)
            v.setBackgroundResource(R.drawable.signinicon1);
        if(v.getId()==R.id.btn_signin && event.getAction()==MotionEvent.ACTION_UP)
            v.setBackgroundResource(R.drawable.signinicon);
        if(v.getId()==R.id.btn_signup && event.getAction()==MotionEvent.ACTION_DOWN)
            v.setBackgroundResource(R.drawable.signupicon1);
        if(v.getId()==R.id.btn_signup && event.getAction()==MotionEvent.ACTION_UP)
            v.setBackgroundResource(R.drawable.signupicon);
        return false;
    }

}


