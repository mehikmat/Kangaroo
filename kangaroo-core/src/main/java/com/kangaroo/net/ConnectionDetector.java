package com.kangaroo.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public  class ConnectionDetector {
    private Context context;

    public ConnectionDetector(Context context){
        this.context = context;
    }

    /*Checking for all possible Internet providers*/
    public boolean isInternetAvailable(){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info & info.isConnectedOrConnecting()){
                return true;
            }
        }
        return false;
    }
}

