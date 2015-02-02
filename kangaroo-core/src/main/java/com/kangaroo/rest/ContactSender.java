package com.kangaroo.rest;

import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import com.kangaroo.R;
import com.kangaroo.net.NetConstant;
import com.kangaroo.net.WebServiceTask;
import com.kangaroo.util.About;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

	public class ContactSender extends Activity implements OnClickListener 
	{   
	     Button send;
	     
	     private static final String TAG = "ContactSender";
	     
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.restclient_layout);
	        send=(Button) findViewById(R.id.btn_send);
	        send.setOnClickListener(this);
	    }      
	     
	    public void postData(View vw) throws InterruptedException, ExecutionException 
	    {	 
			   String pname=System.getProperty("name");
			   String pnumber=System.getProperty("number");
			   WebServiceTask wst = new WebServiceTask(NetConstant.POST_TASK, this, "Posting data...","/person");
	       
		        wst.addNameValuePair("name", pname);
		        wst.addNameValuePair("number", pnumber);
		        wst.addNameValuePair("username", System.getProperty("username").toString());
	     
		        Toast.makeText(this,"Sending"+" "+ pname+" "+pnumber, Toast.LENGTH_LONG).show();	    	 
	      
	        	// the passed String is the URL we will POST to
		        wst.execute(new String[] { wst.getUrl()});	
		        handleResponse(wst.get());
	    }
	 
	    public void handleResponse(String response)
	    {        
	        try {	             
		            JSONObject jso = new JSONObject(response);	             
		            String name = jso.getString("name");
		            String number = jso.getString("number");
		            Toast.makeText(this, name+number, Toast.LENGTH_LONG).show();   
	                 	             
	        	} catch (Exception e) {
	        		Log.e(TAG, e.getLocalizedMessage(), e);
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


