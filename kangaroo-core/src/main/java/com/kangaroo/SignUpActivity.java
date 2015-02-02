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
import com.kangaroo.net.NetConstant;
import com.kangaroo.util.About;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.kangaroo.util.AppConstant.CUSTOMER_ID;
import static com.kangaroo.util.AppConstant.CUSTOMER_NAME;
import static com.kangaroo.util.AppConstant.PASSWORD;
import static com.kangaroo.util.AppConstant.SUCCESS_MSG;

public class SignUpActivity extends Activity implements OnClickListener
{
    // instance variables
    Button btn_signup;
    EditText username;
    EditText password;
    EditText confirmp;
    //class variables
    private static final String TAG = "Sign Up";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        btn_signup=(Button) findViewById(R.id.btn_sign_up);
        btn_signup.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        try {
            postData(v);
        } catch (InterruptedException e) {
            Log.e(TAG, e.getLocalizedMessage());
        } catch (ExecutionException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

    }

    public void postData(View vw) throws InterruptedException, ExecutionException {
        username = (EditText)findViewById(R.id.txt_signup_user_name);
        password = (EditText) findViewById(R.id.txt_signup_password);
        confirmp = (EditText) findViewById(R.id.txt_signup_confirm_password);

        String strUserName = username.getText().toString();
        String strPassword = password.getText().toString();
        String strConfirmPassword = confirmp.getText().toString();

        if(strUserName.isEmpty() || strPassword.isEmpty()) {
            Toast.makeText(this, "Oops!!! Username or Password is empty!", Toast.LENGTH_LONG).show();
            return;
        }

        if(!strPassword.equalsIgnoreCase(strConfirmPassword)){
            Toast.makeText(this, "Password does not match!", Toast.LENGTH_LONG).show();
            return;
        }

        Customer customer = new Customer(strUserName,strUserName);

        Toast.makeText(this,"Registering...", Toast.LENGTH_LONG).show();

        // register customer
        handleResponse(customer, "customer/add");

    }

    public void handleResponse(Customer customer,String endPoint){
        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity entity = null;
        try {
            entity = new StringEntity(customer.toJson());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(getApplicationContext(),NetConstant.BASE_URL + "/" + endPoint,entity,"application/json",
                new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String res = null;
                try {
                    res = new String(response, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (SUCCESS_MSG.equalsIgnoreCase(res)) {
                    // Display successfully registered message using Toast
                    Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
                    goForward();
                }
                // Else display error message
                else {
                    Toast.makeText(getApplicationContext(), "Response: " + response.toString(), Toast.LENGTH_LONG).show();
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

    private void goForward(){
        System.getProperties().put("username",username.getText().toString());
        Intent intent=new Intent(this,OptionActivity.class);
        startActivity(intent);
    }
}

