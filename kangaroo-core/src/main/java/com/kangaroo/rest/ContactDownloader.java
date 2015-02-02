package com.kangaroo.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.kangaroo.PhoneCallerActivity;
import com.kangaroo.util.AppConstant;
import com.kangaroo.R;
import com.kangaroo.net.NetConstant;
import com.kangaroo.net.WebServiceTask;
import com.kangaroo.util.About;

public class ContactDownloader extends ListActivity {
	
	 private static final String TAG = "ContactDownloader";
	 ArrayList<HashMap<String,String>> contactsList = new ArrayList<HashMap<String,String>>();
     Map<String, String> contacts=new HashMap<String, String>();
     
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.import_from_lserver_layout);
	        
	        try {
				getContacts();
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage());
			} catch (ExecutionException e) {
				Log.e(TAG, e.getMessage());

			}
	    } 
	   
	     
	    public void getContacts() throws InterruptedException, ExecutionException {
	 
	    	String username=System.getProperty("username");
	  
	        WebServiceTask wst = new WebServiceTask(NetConstant.POST_TASK, this, "Sending Request to Contact Server...","/person/contacts");
	       
	        wst.addNameValuePair("user", username);	       
	     
	        Toast.makeText(this,"Requesting for contacts", Toast.LENGTH_LONG).show();	    	 
	      
	        // the passed String is the URL we will POST to
	        wst.execute(new String[] { wst.getUrl() });
	        handleResponse(wst.get());
	    }
	 
	    public void handleResponse(String response) {
	    	
	    	if(response.equals("null"))
	    	{		    		
	    		Toast.makeText(this, "no contacts", Toast.LENGTH_LONG).show();	    		
	    	}
	    	else
	    	{
	    		try {	        	 
	                JSONObject json=new JSONObject(response);	                
	                JSONArray peoples = json.getJSONArray("person");	                
		                for (int i = 0; i < peoples.length(); i++)
		                {
								JSONObject p=peoples.getJSONObject(i);						
								String name=p.getString("name");							
								String number=p.getString("number");
								contacts.put(name, number);				
						} 
	        	
		                renderListView(contacts);
	                 	             
	    			} catch (Exception e) {
	    				Log.e(TAG, e.getLocalizedMessage(), e);
	    			}
	    	}
	    } 	     
	     
	 private void renderListView(Map<String, String> contacts)
		{
			Set<String> set=contacts.keySet();
			Iterator<String> it=set.iterator();
			String name;
			String number;
			while (it.hasNext())
				{	

					 name=it.next();
			         number=contacts.get(name);
			         HashMap<String, String> map=new HashMap<String,String>();
			         map.put("username",name);
			         map.put("mobile_no", number);
			         contactsList.add(map);					

				}
		
				ListAdapter adapter = new SimpleAdapter(this,contactsList,
				R.layout.contact_viewer_list_layout, new String[] {"username","mobile_no"}, new int[] {R.id.name, R.id.number,});       				
				setListAdapter(adapter);	        						
		
		}
	   @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		   super.onListItemClick(l, v, position, id);
			String obj1=contactsList.get(position).get("username");
			String obj2=contactsList.get(position).get("mobile_no");
			System.getProperties().put(AppConstant.CONTACT_NAME,obj1);
			System.getProperties().put(AppConstant.CONTACT_NUMBER, obj2);
			
			Intent phoneCaller=new Intent(this,PhoneCallerActivity.class);
			startActivity(phoneCaller);
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
