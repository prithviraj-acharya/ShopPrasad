package com.prithviraj.shopprasad.dataModelClasses;

public class PoojaDataModel {
    int id;
    String pujaName, pujaPrice, pujaImage, percentageOff, offerPrice;

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

    public String getPercentageOff() {
        return percentageOff;
    }

    public void setPercentageOff(String percentageOff) {
        this.percentageOff = percentageOff;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }
}
