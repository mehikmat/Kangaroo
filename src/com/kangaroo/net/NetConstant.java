package com.kangaroo.net;

public interface NetConstant
{
	 public static final int POST_TASK = 1;
     public static final int GET_TASK = 2;
     
     // connection timeout, in milliseconds (waiting to connect)
     public static final int CONN_TIMEOUT = 5000;
      // socket timeout, in milliseconds (waiting for data)
     public static final int SOCKET_TIMEOUT = 5000;
     
     // Base URL for application
     public static final String BASE_URL="http://10.0.2.2:8080/KangarooServices/rest";
}
