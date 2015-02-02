package com.kangaroo.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class WebServiceTask extends AsyncTask<String, Integer, String>
{
	//instance variables
	 private int taskType = NetConstant.GET_TASK;
     private Context mContext = null;
     private String processMessage = "Processing...";
     private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
     private ProgressDialog pDlg = null;
     private String  url=null;
     
     //class variables
     private static final String TAG = "WebServiceTask";
     
     // Constructor 
     public WebServiceTask(int taskType, Context mContext, String processMessage,String url)
     {

         this.taskType = taskType;
         this.mContext = mContext;
         this.processMessage = processMessage;
         this.setUrl(url);
     }

     public void addNameValuePair(String name, String value)
     {
        
         params.add(new BasicNameValuePair(name, value));
         
     }
    
     @Override
     protected void onPreExecute()
     {   super.onPreExecute();	
     	 showProgressDialog();
     }

     protected String doInBackground(String... urls)
     {
         String url = urls[0];
         String result = "";
         
         HttpResponse response = doResponse(url);

         if (response == null)
         {
             return result;
         }
         else 
         {
             try {

               	  result = inputStreamToString(response.getEntity().getContent());

             	 } catch (IllegalStateException e)
             	 {
             		 Log.e(TAG, e.getLocalizedMessage(), e);

            	 } catch (IOException e)
            	 {
                	 Log.e(TAG, e.getLocalizedMessage(), e);
            	 }

         }

         return result;
     }
     
     private HttpResponse doResponse(String url)
     {         
         HttpClient httpclient = new DefaultHttpClient(getHttpParams());
         HttpResponse response = null;
         try {
             switch (taskType)
             {
             	case NetConstant.POST_TASK:
	                 HttpPost httppost = new HttpPost(url);
	                 // Add parameters
	                 httppost.setEntity(new UrlEncodedFormEntity(params));
	                 response = httpclient.execute(httppost);
	                 break;
             	case NetConstant.GET_TASK:
	                 HttpGet httpget = new HttpGet(url);
	                 response = httpclient.execute(httpget);
	                 break;
             }
           } catch (Exception e)
           		{
        	 	  Log.e(TAG, e.getLocalizedMessage(), e);
        		}
         return response;
     }
         
     // Establish connection and socket (data retrieval) timeouts
     private HttpParams getHttpParams()
     {          
         HttpParams htpp = new BasicHttpParams();
          
         HttpConnectionParams.setConnectionTimeout(htpp, NetConstant.CONN_TIMEOUT);
         HttpConnectionParams.setSoTimeout(htpp, NetConstant.SOCKET_TIMEOUT);
          
         return htpp;
     }
      
      private String inputStreamToString(InputStream is) 
     	 {
		    	 String line = "";
		         StringBuilder total = new StringBuilder();
		
		         // Wrap a BufferedReader around the InputStream
		         BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		
		         try {
		             // Read response until the end
		             while ((line = rd.readLine()) != null) {
		                 total.append(line);
		             }
		         } catch (IOException e) {
		             Log.e(TAG, e.getLocalizedMessage(), e);
		         }
		
		         // Return full string
		         return total.toString();
    	 }
     
     
     private void showProgressDialog()
     {         
         pDlg = new ProgressDialog(mContext);
         pDlg.setTitle("Kangaroo..");
         pDlg.setMessage(processMessage);
         pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
         pDlg.setCancelable(false);
         pDlg.show();
     }
     @Override
     protected void onPostExecute(String result)
  	 {	
    	 super.onPostExecute(result);
    	 pDlg.dismiss();
  	 }
    	
  	 
	public String getUrl()
	{
		return this.url;
	}

	public void setUrl(String url) 
	{
		StringBuilder buildURI=new StringBuilder(NetConstant.BASE_URL);
		buildURI.append(url);		
		this.url =buildURI.toString();;
	}
	
}
