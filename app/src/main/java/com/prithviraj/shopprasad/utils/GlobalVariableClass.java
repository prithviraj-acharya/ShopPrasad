package com.prithviraj.shopprasad.utils;


import com.prithviraj.shopprasad.interfaces.AddPanditProfilePujaInterfaces;
import com.prithviraj.shopprasad.interfaces.AddToCartFeaturedProductList;
import com.prithviraj.shopprasad.interfaces.AddToCartPoojaSamagriList;
import com.prithviraj.shopprasad.interfaces.AddToCartProductList;
import com.prithviraj.shopprasad.interfaces.AddressButtonInterfaces;
import com.prithviraj.shopprasad.interfaces.ClickFeaturedProductList;
import com.prithviraj.shopprasad.interfaces.ClickForProductDetails;
import com.prithviraj.shopprasad.interfaces.ClickPoojaList;
import com.prithviraj.shopprasad.interfaces.ClickPoojaSamagriList;
import com.prithviraj.shopprasad.interfaces.ClickProductList;
import com.prithviraj.shopprasad.interfaces.DecreaseItemQuantityFromCart;
import com.prithviraj.shopprasad.interfaces.IncreaseItemQuantityFromCart;
import com.prithviraj.shopprasad.interfaces.OrderHistoryClickInterface;
import com.prithviraj.shopprasad.interfaces.SelectAddressButtonInterfaces;
import com.prithviraj.shopprasad.interfaces.SelectAddressForCheckout;

public class GlobalVariableClass {
    public ClickProductList clickProductList;
    public ClickPoojaSamagriList clickPoojaSamagriList;
    public AddToCartProductList  addToCartProductList;
    public AddToCartPoojaSamagriList addToCartPoojaSamagriList;
    public IncreaseItemQuantityFromCart increaseItemQuantityFromCart;
    public DecreaseItemQuantityFromCart decreaseItemQuantityFromCart;
    public AddressButtonInterfaces addressButtonInterfaces;
    public AddPanditProfilePujaInterfaces addPanditProfilePujaInterfaces;
    public SelectAddressButtonInterfaces selectAddressButtonInterfaces;
    public OrderHistoryClickInterface orderHistoryClickInterface;
    public ClickPoojaList clickPoojaList;
    public ClickForProductDetails clickForProductDetails;
    public ClickFeaturedProductList clickFeaturedProductList;
    public AddToCartFeaturedProductList addToCartFeaturedProductList;
    public SelectAddressForCheckout selectAddressForCheckout;

    public int poojaId =0;
    public String poojaPrice = "";
}
