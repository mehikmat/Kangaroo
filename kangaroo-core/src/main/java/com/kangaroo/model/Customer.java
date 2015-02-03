package com.kangaroo.model;

import com.kangaroo.util.AppConstant;

/**
 * Created by hikmat on 2/3/15.
 */
public class Customer {

    private String customerId;
    private String password;

    public Customer(){}

    public Customer(String customerId, String password) {
        this.customerId = customerId;
        this.password = password;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toJson(){
        StringBuilder json = new StringBuilder();
        json
            .append("{")
            .append("\"").append(AppConstant.CUSTOMER_ID).append("\"").append(":").append("\"").append(customerId).append("\"") .append(",")
            .append("\"").append(AppConstant.PASSWORD).append("\"").append(":").append("\"").append(password).append("\"")
            .append("}");

        return json.toString();
    }
}
