package com.prithviraj.shopprasad.dataModelClasses;

public class ProductDataModel {
    int id;
    String pujaName, pujaPrice, pujaImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPujaName() {
        return pujaName;
    }

    public void setPujaName(String pujaName) {
        this.pujaName = pujaName;
    }

    public String getPujaPrice() {
        return pujaPrice;
    }

    public void setPujaPrice(String pujaPrice) {
        this.pujaPrice = pujaPrice;
    }

    public String getPujaImage() {
        return pujaImage;
    }

    public void setPujaImage(String pujaImage) {
        this.pujaImage = pujaImage;
    }
}
