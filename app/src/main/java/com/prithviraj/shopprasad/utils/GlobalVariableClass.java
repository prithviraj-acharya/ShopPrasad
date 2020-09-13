package com.prithviraj.shopprasad.utils;


import com.prithviraj.shopprasad.interfaces.AddToCartPoojaSamagriList;
import com.prithviraj.shopprasad.interfaces.AddToCartProductList;
import com.prithviraj.shopprasad.interfaces.ClickPoojaSamagriList;
import com.prithviraj.shopprasad.interfaces.ClickProductList;
import com.prithviraj.shopprasad.interfaces.DecreaseItemQuantityFromCart;
import com.prithviraj.shopprasad.interfaces.IncreaseItemQuantityFromCart;

public class GlobalVariableClass {
    public ClickProductList clickProductList;
    public ClickPoojaSamagriList clickPoojaSamagriList;
    public AddToCartProductList  addToCartProductList;
    public AddToCartPoojaSamagriList addToCartPoojaSamagriList;
    public IncreaseItemQuantityFromCart increaseItemQuantityFromCart;
    public DecreaseItemQuantityFromCart decreaseItemQuantityFromCart;
}
