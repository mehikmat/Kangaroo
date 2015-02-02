package com.kangaroo.model;

import com.kangaroo.util.AppConstant;

import java.util.Currency;

/**
 * Created by hikmat on 2/3/15.
 */
public class Customer {

    private String customerId;
    private String customerName;

    public Customer(){}

    public Customer(String customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String toJson(){
        StringBuilder json = new StringBuilder();
        json
            .append("{")
            .append("\"").append(AppConstant.CUSTOMER_ID).append("\"").append(":").append("\"").append(customerId).append("\"") .append(",")
            .append("\"").append(AppConstant.CUSTOMER_NAME).append("\"").append(":").append("\"").append(customerName).append("\"")
            .append("}");

        return json.toString();
    }

    public static void main(String[] args) {
        System.out.println(new Customer("hk","dd").toJson());
    }
}
