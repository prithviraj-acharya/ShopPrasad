package com.prithviraj.shopprasad.utils;

public class Url {

    private static final String LIVE_URL = "";
    private static final String STAGING_URL = "http://shopprasad.herokuapp.com/public/api/";

    private static final String CMS_HOME_URL = "";

    private static final String BASE_URL = STAGING_URL;

    public static final String REGISTRATION_URL = BASE_URL + "register";
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String VERIFY_OTP = BASE_URL + "verify-user";
    public static final String RESEND_OTP = BASE_URL + "resend-otp";

    //Customer
    public static final String PUJA_LIST = BASE_URL + "puja/list";
    public static final String PRODUCT_LIST = BASE_URL + "product/list";
    public static final String SAMAGRI_LIST = BASE_URL + "product/puja-samagri";
    public static final String PANDIT_LIST = BASE_URL + "pandit/list";
    public static final String UPLOAD_ORDER = BASE_URL + "upload/order";
    public static final String ASTROLOGY_STORE = BASE_URL + "astrology/store";
    public static final String PROFILE = BASE_URL + "customer/profile";
    public static final String PROFILE_UPDATE = BASE_URL + "customer/profile/update";
    public static final String PROFILE_IMAGE_UPDATE = BASE_URL + "customer/profile/image";
    public static final String PRODUCT_DETAILS = BASE_URL + "product/";
    public static final String STATES = BASE_URL + "customer/states";
    public static final String MY_ADDRESS_LIST = BASE_URL + "customer/address/list";
    public static final String ADD_NEW_ADDRESS = BASE_URL + "customer/address/store";
    public static final String UPDATE_ADDRESS = BASE_URL + "customer/address/update";
    public static final String REMOVE_ADDRESS = BASE_URL + "customer/address/";


    //Customer Cart
    public static final String ADD_TO_CART = BASE_URL + "cart/store";
    public static final String MY_CART = BASE_URL + "cart/list";
    public static final String REMOVE_ITEMS_FROM_CART = BASE_URL + "cart/";
    public static final String UPDATE_ITEMS_TO_CART = BASE_URL + "cart/update";


}
