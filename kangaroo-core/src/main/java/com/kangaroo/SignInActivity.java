package com.kangaroo;

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

import com.kangaroo.model.Customer;
import com.kangaroo.net.ConnectionDetector;
import com.kangaroo.net.NetConstant;
import com.kangaroo.util.About;
import com.kangaroo.util.AlertDialogManager;
import com.kangaroo.util.CommonUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import static com.kangaroo.util.AppConstant.CUSTOMER_ID;
import static com.kangaroo.util.AppConstant.STATUS;
import static com.kangaroo.util.AppConstant.SUCCESS_MSG;

public class SignInActivity extends Activity implements OnClickListener,NetConstant {
    Button btn_signin;
    EditText username;
    EditText password;

    ConnectionDetector cd = null;
    AlertDialogManager alert = null;
    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        btn_signin = (Button) findViewById(R.id.btn_sign_in);
        btn_signin.setOnClickListener(this);
    }


    public void postData(View vw) throws InterruptedException, ExecutionException {

        username = (EditText) findViewById(R.id.txt_signin_user_name);
        password = (EditText) findViewById(R.id.txt_signin_password);
        String t1 = username.getText().toString();
        String t2 = password.getText().toString();

        Customer customer = new Customer(t1, t2);
        Toast.makeText(this, "Verifying Username and Password", Toast.LENGTH_SHORT).show();

        // the passed String is the URL we will POST to
        handleResponse(customer, "customer/validate");
    }

    public void handleResponse( Customer customer, String endPoint) {
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
                            JSONObject jsonObject = new JSONObject(new String(response));
                            if (SUCCESS_MSG.equalsIgnoreCase(jsonObject.getString(STATUS))) {
                                // Display successfully registered message using Toast
                                Toast.makeText(getApplicationContext(), "Sign in Success!", Toast.LENGTH_SHORT).show();
                                goForward();
                            }
                            // Else display error message
                            else {
                                Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), "Unexpected Error occurred! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void goForward(){
        System.getProperties().put(CUSTOMER_ID,username.getText().toString());
        Intent i = new Intent(this, OptionActivity.class);
        startActivity(i);
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
