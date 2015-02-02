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
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (null != info)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
        }
        return false;
    }
}
