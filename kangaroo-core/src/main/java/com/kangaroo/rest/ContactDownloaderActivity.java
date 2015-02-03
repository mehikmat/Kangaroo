package com.kangaroo.rest;

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
import com.kangaroo.R;
import com.kangaroo.model.Customer;
import com.kangaroo.net.NetConstant;
import com.kangaroo.util.About;
import com.kangaroo.util.AppConstant;
import com.kangaroo.util.CommonUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class ContactDownloaderActivity extends ListActivity {

    private static final String TAG = "ContactDownloaderActivity";
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
        String username = System.getProperty(AppConstant.CUSTOMER_ID);

        Customer customer = new Customer(username,"empty");
        Toast.makeText(this,"Downloading Contacts...", Toast.LENGTH_LONG).show();

        // the passed String is the URL we will POST to
        handleResponse(customer,"contact/list");
    }

    public void handleResponse(Customer customer, String endPoint) {
        AsyncHttpClient client = CommonUtil.initAsyncHttpClient();
        StringEntity entity = null;
        try {
            entity = new StringEntity(customer.toJson());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(getApplicationContext(), NetConstant.BASE_URL + "/" + endPoint, entity, "application/json",
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        try {
                            JSONArray peoples = new JSONArray( new String(response));
                            if (peoples.length() > 0){
                                for (int i = 0; i < peoples.length(); i++) {
                                    JSONObject p = peoples.getJSONObject(i);
                                    String name = p.getString(AppConstant.CONTACT_NAME);
                                    String number = p.getString(AppConstant.CONTACT_NUMBER);
                                    contacts.put(name, number);
                                }
                            }else {
                                Toast.makeText(new ContactDownloaderActivity(),"No Contacts", Toast.LENGTH_SHORT).show();
                            }
                            renderListView(contacts);
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
                            Toast.makeText(getApplicationContext(), "Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void renderListView(Map<String, String> contacts){
        Set<String> set=contacts.keySet();
        Iterator<String> it = set.iterator();
        String name;
        String number;
        while (it.hasNext()) {
            name = it.next();
            number = contacts.get(name);
            HashMap<String, String> map=new HashMap<String,String>();
            map.put(AppConstant.CONTACT_NAME,name);
            map.put(AppConstant.CONTACT_NUMBER, number);
            contactsList.add(map);
        }
        ListAdapter adapter = new SimpleAdapter(this,contactsList,
                R.layout.contact_viewer_list_layout, new String[] {AppConstant.CONTACT_NAME,AppConstant.CONTACT_NUMBER}, new int[] {R.id.contactName, R.id.contactNumber,});
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String obj1 = contactsList.get(position).get(AppConstant.CONTACT_NAME);
        String obj2 = contactsList.get(position).get(AppConstant.CONTACT_NUMBER);
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
