package com.prithviraj.shopprasad.dataModelClasses;

import java.util.ArrayList;

public class PanditProfilePujaDataModel {

    String pujaName, addedPujaPrice;
    int pujaId;
    ArrayList<String> pujaList;

    public String getPujaName() {
        return pujaName;
    }

    public void setPujaName(String pujaName) {
        this.pujaName = pujaName;
    }

    public String getAddedPujaPrice() {
        return addedPujaPrice;
    }

    public void setAddedPujaPrice(String addedPujaPrice) {
        this.addedPujaPrice = addedPujaPrice;
    }

    public int getPujaId() {
        return pujaId;
    }

    public void setPujaId(int pujaId) {
        this.pujaId = pujaId;
    }

    public ArrayList<String> getPujaList() {
        return pujaList;
    }

    public void setPujaList(ArrayList<String> pujaList) {
        this.pujaList = pujaList;
    }
}
