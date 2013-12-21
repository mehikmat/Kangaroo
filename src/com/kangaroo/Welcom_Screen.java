package com.kangaroo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.kangaroo.event.EventHandler;
import com.kangaroo.net.ConnectionDetector;
import com.kangaroo.utility.About;
import com.kangaroo.utility.AlertDialogManager;

public class Welcom_Screen extends Activity 
{
	Button btn_signin;
	Button btn_signup;
	ConnectionDetector cd;
	ProgressDialog pd;
	AlertDialogManager alert=new AlertDialogManager();
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen_layout);
		checkConnctivity();
		btn_signin=(Button) findViewById(R.id.btn_signin);
		btn_signin.setOnClickListener(new EventHandler(this));
		btn_signin.setOnTouchListener(new EventHandler(this));
		btn_signup=(Button) findViewById(R.id.btn_signup);
		btn_signup.setOnClickListener(new EventHandler(this));
		btn_signup.setOnTouchListener(new EventHandler(this));
		
		
	}
	private void checkConnctivity() {
		cd=new ConnectionDetector(getApplicationContext());
		showProgressDialog();
		if(!cd.isConnectingToInternet())
		{
			// Internet Connection is not present
            alert.showAlertDialog(this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
		}		 
		 pd.dismiss();		
	}
	
	private void showProgressDialog() {
		pd=new ProgressDialog(this);
		pd.setTitle("Network Info...");
		pd.setMessage("Checking Network Connection");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.show();		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.app_option, menu);
		return true ;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		return applyMenuChoices(item)|| super.onOptionsItemSelected(item);
	}
	private boolean applyMenuChoices(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			Intent aboutIntent=new Intent(this,About.class);
			startActivity(aboutIntent);
			break;
		case R.id.exit:
			Intent exitIntent=new Intent(Intent.ACTION_MAIN);
			exitIntent.addFlags(exitIntent.FLAG_ACTIVITY_CLEAR_TOP);
			exitIntent.addCategory(exitIntent.CATEGORY_HOME);
			startActivity(exitIntent);

		default:
			break;
		}
		return true;
	}

}
