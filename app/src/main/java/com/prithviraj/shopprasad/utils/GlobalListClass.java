package com.prithviraj.shopprasad.utils;

import com.prithviraj.shopprasad.dataModelClasses.AddressDataModel;
import com.prithviraj.shopprasad.dataModelClasses.CartDataModel;
import com.prithviraj.shopprasad.dataModelClasses.OrderHitoryDataModel;
import com.prithviraj.shopprasad.dataModelClasses.PanditProfilePujaDataModel;
import com.prithviraj.shopprasad.dataModelClasses.PoojaDataModel;

import java.util.ArrayList;

public class GlobalListClass {

    public ArrayList<PoojaDataModel> poojaList = new ArrayList<>();
    public ArrayList<PoojaDataModel> productList = new ArrayList<>();
    public ArrayList<PoojaDataModel> poojaaSamagriList = new ArrayList<>();
    public ArrayList<PoojaDataModel> panditList = new ArrayList<>();
    public ArrayList<CartDataModel> cartList = new ArrayList<>();
    public ArrayList<AddressDataModel> addressList = new ArrayList<>();
    public ArrayList<PanditProfilePujaDataModel> panditPujaList = new ArrayList<>();
    public ArrayList<String> staticPujaList = new ArrayList<>();
    public ArrayList<OrderHitoryDataModel> orderHistoryList = new ArrayList<>();
    public ArrayList<PoojaDataModel> featuredProductList = new ArrayList<>();

}
