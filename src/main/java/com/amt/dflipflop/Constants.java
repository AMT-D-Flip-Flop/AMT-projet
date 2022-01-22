package com.amt.dflipflop;

public final class Constants {

    // used as keys for redirectFlashAttributes
    public final static String SUCCESS_MSG_KEY = "success";
    public final static String ERROR_MSG_KEY = "error";
    // Production or not
    public final static Boolean IS_PROD = false;

    //Prod
    public final static String jwtfileNamePath = "/opt/tomcat/webapps/zone_secret/jwt.txt";

    //Local Use only
    public final static String tokenSecretDefault = "secret";

    public final static String ROLE_ADMIN = "ROLE_ADMIN";


}
