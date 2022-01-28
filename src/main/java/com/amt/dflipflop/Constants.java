/**
 * Date de création     : Janvier2021
 * Groupe               : AMT-D-Flip-Flop
 * Description          : Constantes de l'application
 */

package com.amt.dflipflop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {

    // used as keys for redirectFlashAttributes
    public final static String SUCCESS_MSG_KEY = "success";
    public final static String ERROR_MSG_KEY = "error";

    public static Boolean IS_PROD;

    //Prod
    public final static String jwtfileNamePath = "/opt/tomcat/zone_secret/jwt.txt";

    //Local Use only
    public final static String tokenSecretDefault = "secret";

    @Value("${app.isProd}")
    public void setProd(boolean prod){
        Constants.IS_PROD = prod;
    }

}
