package com.kangaroo;

import java.util.concurrent.ExecutionException;

import com.kangaroo.net.ConnectionDetector;
import com.kangaroo.net.NetConstant;
import com.kangaroo.net.WebServiceTask;
import com.kangaroo.util.About;
import com.kangaroo.util.AlertDialogManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends Activity implements OnClickListener,NetConstant {
	 Button btn_signin;
	 EditText username;
	 EditText password;
	 EditText confirmp;
	 
	 ConnectionDetector cd=null;
	 AlertDialogManager alert=null;
	 private static final String TAG = "SignInActivity";
	 
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin_layout);
		btn_signin=(Button) findViewById(R.id.btn_sign_in);
		btn_signin.setOnClickListener(this);
	}
	 
	 
	 public void postData(View vw) throws InterruptedException, ExecutionException {
		   
		   username=(EditText)findViewById(R.id.txt_signin_user_name);
		   password=(EditText) findViewById(R.id.txt_signin_password);
		   confirmp=(EditText) findViewById(R.id.txt_signin_confirm_password);
		   String t1=password.getText().toString();
		   String t2=confirmp.getText().toString();
		   if(!t1.equalsIgnoreCase(t2))
			{
			   Toast.makeText(this, "password does not match", Toast.LENGTH_LONG).show();
			   return;
			}
	       WebServiceTask wst = new WebServiceTask(POST_TASK,this,"Sending name and password...","/auth");
		   
	       wst.addNameValuePair("username",username.getText().toString());
	       wst.addNameValuePair("password",password.getText().toString());
	     
	       Toast.makeText(this,"verifying user name and password...", Toast.LENGTH_LONG).show();
	       
	       // the passed String is the URL we will POST to
	       wst.execute(new String[] { wst.getUrl() });
	       handleResponse(wst.get());
	       
	      
	 
	    }
	 
	    public void handleResponse(String response) 
	    {        
	        try {
			             System.getProperties().put("username",username.getText().toString());
			             if(response.toString().equalsIgnoreCase("ok"))
			             {
			            	 Intent i=new Intent(this,OptionActivity.class);
			         
			            	 startActivity(i);
			          	 }
			          	 else if (response.toString().equalsIgnoreCase("invalid"))
			          	 {
			        	  
			        	   new AlertDialogManager().showAlertDialog(this, "Kangaroo..","user name or password is not correct!!!",true);
			          	 }   
			          	 else
			          		 Toast.makeText(this, "Connection Error...", Toast.LENGTH_LONG).show();
	                 	             
	        	} catch (Exception e) {
	        			Log.e(TAG, e.getLocalizedMessage().toString());
	        	}	         
	    }
	 
	    @Override
		public void onClick(View v)
	    {
			try {
					postData(v);
				} catch (InterruptedException e) {
				    Log.e(TAG, e.getMessage().toString());
				
				} catch (ExecutionException e) {
					Log.e(TAG, e.getMessage().toString());					
				}
		
		}
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
