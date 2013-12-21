package com.kangaroo;

import java.util.concurrent.ExecutionException;

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

import com.kangaroo.net.NetConstant;
import com.kangaroo.net.WebServiceTask;
import com.kangaroo.utility.About;
import com.kangaroo.utility.AlertDialogManager;

public class SignUp extends Activity implements OnClickListener 
{
	// instance variables
	 Button btn_signup;
	 EditText username;
	 EditText password;
	 EditText confirmp;
	 //class variables
	private static final String TAG = "Sign Up";
	
		@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_layout);
		btn_signup=(Button) findViewById(R.id.btn_sign_up);
		btn_signup.setOnClickListener(this);
	}
	  
	public void postData(View vw) throws InterruptedException, ExecutionException
	{	 
		   username=(EditText)findViewById(R.id.txt_signup_user_name);
		   password=(EditText) findViewById(R.id.txt_signup_password);
		   confirmp=(EditText) findViewById(R.id.txt_signup_confirm_password);
		   String t0=username.getText().toString();
		   String t1=password.getText().toString();
		   String t2=confirmp.getText().toString();
		   if(t0.isEmpty()||t1.isEmpty())
		   {
			   Toast.makeText(this, "Oops!!! user name or passward cannot be empty!", Toast.LENGTH_LONG).show();
			   return;
		   }
		   if(!t1.equalsIgnoreCase(t2))
			{
			   Toast.makeText(this, "password does not match", Toast.LENGTH_LONG).show();
			   return;
			}
		   
	       WebServiceTask wst = new WebServiceTask(NetConstant.POST_TASK, this, "Sending name and password...","/reg");
	 
	       wst.addNameValuePair("username",username.getText().toString());
	       wst.addNameValuePair("password",password.getText().toString());
	     
	       Toast.makeText(this,"registering user...", Toast.LENGTH_LONG).show();
	    		      
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
		             else if (response.toString().equalsIgnoreCase("calceled"))
			           {
			        	   Toast.makeText(this,"Server Error...", Toast.LENGTH_LONG).show();
			        	   return;
			           }
		             else if(response.toString().equalsIgnoreCase("AlreadyRegistered"));
			           {
			        	   new AlertDialogManager().showAlertDialog(this,"Kangaroo...","Already Registered!!!",false);
			        	   return;
			           }
			          			           
		        } catch (Exception e) {
		            Log.e(TAG, e.getMessage());
		        }	         
	    }
	 
	 @Override
	 public void onClick(View v)
   	  {
		try {
				postData(v);
				
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			} catch (ExecutionException e) {
				Log.e(TAG, e.getMessage());
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
