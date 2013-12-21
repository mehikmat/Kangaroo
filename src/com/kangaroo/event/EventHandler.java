package com.kangaroo.event;

import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.kangaroo.AppConstant;
import com.kangaroo.ContactViewer;
import com.kangaroo.OptionActivity;
import com.kangaroo.R;
import com.kangaroo.SignIn;
import com.kangaroo.SignUp;
import com.kangaroo.Welcom_Screen;
import com.kangaroo.rest.ContactDownloader;
import com.kangaroo.utility.PhoneCaller;

public class EventHandler implements OnClickListener, OnTouchListener 
{
    OptionActivity oa;
    Welcom_Screen ws;
    SignIn si;
    PhoneCaller pc;
          
	public EventHandler(OptionActivity oa)
	{
		this.oa=oa;
	}
	public EventHandler(Welcom_Screen ws) {
		this.ws=ws;
	}
	public EventHandler(SignIn si) {
		this.si=si;
	}
	public EventHandler(PhoneCaller pc)
	{
		this.pc=pc;
	}
	
	@Override
	public void onClick(View view)
	{	

		if(view.getId()==R.id.btn_import_from_phone)
		{
		Intent viewIntent=new Intent(oa,ContactViewer.class);
		oa.startActivity(viewIntent);	
		}
		if(view.getId()==R.id.btn_import_from_server)
		{
			Intent addIntent=new Intent(oa,ContactDownloader.class);
			oa.startActivity(addIntent);
			
		}
		if(view.getId()==R.id.btn_signin)
		{
			Intent signinIntent=new Intent(ws,SignIn.class);
			ws.startActivity(signinIntent);
		}
		

		if(view.getId()==R.id.btn_signup)
		{
			Intent signupIntent=new Intent(ws,SignUp.class);
			ws.startActivity(signupIntent);
		}
		if(view.getId()==R.id.btn_phone_call)
		{
			Intent phoneCall=new Intent(Intent.ACTION_CALL);
			phoneCall.setData(Uri.parse("tel:"+System.getProperty(AppConstant.NUMBER_KEY).toString()));
			pc.startActivity(phoneCall);
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


