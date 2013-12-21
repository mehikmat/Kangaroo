package com.kangaroo.utility;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.kangaroo.R;
import com.kangaroo.event.EventHandler;

public class PhoneCaller extends Activity{
	Button phoneCaller;
	@Override

protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	setContentView(R.layout.phone_caller_layout);
	phoneCaller=(Button) findViewById(R.id.btn_phone_call);
	phoneCaller.setOnClickListener(new EventHandler(this));
}

}
