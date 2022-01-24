package com.amt.dflipflop;

import org.springframework.beans.factory.annotation.Value;

public final class Constants {

    // used as keys for redirectFlashAttributes
    public final static String SUCCESS_MSG_KEY = "success";
    public final static String ERROR_MSG_KEY = "error";
    public final static String mode = "prod";
    //public final static String mode = "noProd";

    // Production or not
    @Value("app.is_prod")
    public final static Boolean IS_PROD = false;


}
