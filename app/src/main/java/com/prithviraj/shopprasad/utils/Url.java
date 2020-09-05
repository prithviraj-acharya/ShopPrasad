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
    public static final String PANDIT_LIST = BASE_URL + "pandit/list";
    public static final String UPLOAD_ORDER = BASE_URL + "upload/order";




}
