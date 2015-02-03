package com.kangaroo.util;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by hikmat on 2/3/15.
 */
public class CommonUtil {
    private static final int HTTP_TIMEOUT = 80000;

    public static AsyncHttpClient initAsyncHttpClient(){
        AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
        localAsyncHttpClient.setTimeout(80000);
        return localAsyncHttpClient;
    }

    public static String getDeviceId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    private String getPhoneNumber(Context context){
        Object localObject = "not available";
        try {
            TelephonyManager localTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if ((localTelephonyManager != null) && (localTelephonyManager.getSimState() != 1)) {
                String str = localTelephonyManager.getLine1Number();
                localObject = str;
            }
            return String.valueOf(localObject);
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return String.valueOf(localObject);
    }
}
