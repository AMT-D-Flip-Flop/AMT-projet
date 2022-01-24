/**
 * Date de création     : Janvier2021
 * Groupe               : AMT-D-Flip-Flop
 * Description          : Constantes de l'application
 */

package com.amt.dflipflop;

import org.springframework.beans.factory.annotation.Value;

public final class Constants {

    // used as keys for redirectFlashAttributes
    public final static String SUCCESS_MSG_KEY = "success";
    public final static String ERROR_MSG_KEY = "error";
    // Production or not
    @Value("app.is_prod")
    public final static Boolean IS_PROD = false;

    //Prod
    public final static String jwtfileNamePath = "/opt/tomcat/webapps/zone_secret/jwt.txt";

    //Local Use only
    public final static String tokenSecretDefault = "secret";

}
