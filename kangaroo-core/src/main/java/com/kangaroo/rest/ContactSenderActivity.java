package com.kangaroo.rest;

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

import com.kangaroo.R;
import com.kangaroo.model.Contact;
import com.kangaroo.net.NetConstant;
import com.kangaroo.util.About;
import com.kangaroo.util.AppConstant;
import com.kangaroo.util.CommonUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import static com.kangaroo.util.AppConstant.STATUS;
import static com.kangaroo.util.AppConstant.SUCCESS_MSG;

public class ContactSenderActivity extends Activity implements OnClickListener{
    Button send;

    private static final String TAG = "ContactSenderActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restclient_layout);
        send=(Button) findViewById(R.id.btn_send);
        send.setOnClickListener(this);
    }

    public void postData(View vw) throws InterruptedException, ExecutionException {

        String cname=System.getProperty(AppConstant.CUSTOMER_ID);
        String pname=System.getProperty(AppConstant.CONTACT_NAME);
        String pnumber=System.getProperty(AppConstant.CONTACT_NUMBER);

        Contact contact = new Contact(cname,pname,pnumber);

        Toast.makeText(this,"Sending"+" "+ pname+" "+pnumber, Toast.LENGTH_LONG).show();

        // the passed String is the URL we will POST to
        handleResponse(contact, "contact/add");
    }

    public void handleResponse(Contact contact, String endPoint){
        AsyncHttpClient client = CommonUtil.initAsyncHttpClient();
        StringEntity entity = null;
        try {
            entity = new StringEntity(contact.toJson());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(getApplicationContext(),NetConstant.BASE_URL + "/" + endPoint,entity,"application/json",
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response));
                            if (SUCCESS_MSG.equalsIgnoreCase(jsonObject.getString(STATUS))) {
                                // Display successfully registered message using Toast
                                Toast.makeText(getApplicationContext(), "Upload Success!", Toast.LENGTH_SHORT).show();
                            }
                            // Else display error message
                            else {
                                Toast.makeText(getApplicationContext(), "Upload Failed!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // When the response returned by REST has Http response code other than '200'
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(getApplicationContext(),"Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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


