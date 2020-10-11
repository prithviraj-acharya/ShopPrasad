package com.prithviraj.shopprasad.dataModelClasses;

import java.util.ArrayList;

public class OrderHitoryDataModel {

    int id, otp;
    String orderId, name, address, phone, paymentType, status, deliveryBoyName, deliveryBoyNumber, amountPaid;
    ArrayList<ProductDataModel> itemsOrdered;


    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryBoyName() {
        return deliveryBoyName;
    }

    public void setDeliveryBoyName(String deliveryBoyName) {
        this.deliveryBoyName = deliveryBoyName;
    }

    public String getDeliveryBoyNumber() {
        return deliveryBoyNumber;
    }

    public void setDeliveryBoyNumber(String deliveryBoyNumber) {
        this.deliveryBoyNumber = deliveryBoyNumber;
    }

    public ArrayList<ProductDataModel> getItemsOrdered() {
        return itemsOrdered;
    }

    public void setItemsOrdered(ArrayList<ProductDataModel> itemsOrdered) {
        this.itemsOrdered = itemsOrdered;
    }
}
