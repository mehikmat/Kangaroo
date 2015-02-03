package com.kangaroo.model;

import com.kangaroo.util.AppConstant;

/**
 * Created by hikmat on 2/3/15.
 */
public class Contact {

    private String customerId;
    private String contactName;
    private String contactNumber;

    public Contact(){}

    public Contact(String customerId, String password, String contactNumber) {
        this.customerId = customerId;
        this.contactName = password;
        this.contactNumber = contactNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String toJson(){
        StringBuilder json = new StringBuilder();
        json
            .append("{")
            .append("\"").append(AppConstant.CUSTOMER_ID).append("\"").append(":").append("\"").append(customerId).append("\"") .append(",")
            .append("\"").append(AppConstant.CONTACT_NAME).append("\"").append(":").append("\"").append(contactName).append("\"").append(",")
            .append("\"").append(AppConstant.CONTACT_NUMBER).append("\"").append(":").append("\"").append(contactNumber).append("\"")
            .append("}");

        return json.toString();
    }
}
