/**
 * Date de création     : 06.12.2021
 * Groupe               : AMT-D-Flip-Flop
 * Description          : Implémentation de TokenProvider
 * Remarque             : -
 * Sources : TCMALTUNKAN - MEHMET ANIL ALTUNKAN
 */

package security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;


@Service
public class TokenProviderImpl implements TokenProvider {
    Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    //@Value("${authentication-test.auth.tokenSecret}")
    private String tokenSecret = "secret";

    private boolean keyGenerated;

    //@Value(value = "${mode.choice}")

    //Define the key choice for jwt
    //private String mode = "prod";
    private String mode = "noProd";
    private String jwtfileNamePath = "/opt/tomcat/webapps/zone_secret/jwt.txt";


    private LinkedHashMap lp;

    public TokenProviderImpl() {
        this.keyGenerated = false;
    }


    //Read file content into the string with - Files.lines(Path path, Charset cs)
    private static String readLine(String filePath) throws IOException {
        // File path is passed as parameter
        File file = new File(
                filePath);

        // Creating an object of BufferedReader class
        BufferedReader br
                = new BufferedReader(new FileReader(file));

        return br.readLine();
    }

    void generateKey() throws IOException {
        if (!keyGenerated && mode.equals("prod")) {
            logger.error("reade file");
            tokenSecret = readLine(jwtfileNamePath);
            keyGenerated = true;
        }
    }


    @Override
    public HashMap getAccountFromToken(String token) throws Exception {
        generateKey();
        Claims claims = Jwts.parser().setSigningKey(tokenSecret.getBytes(Charset.forName("UTF-8"))).parseClaimsJws(token).getBody();
        logger.error(claims.toString());
        lp = new LinkedHashMap();
        lp.put("username", claims.getSubject());
        lp.put("role", claims.get("role"));
        if (lp == null) {
            throw new Exception("User hashamp is null 1");
        }
        return lp;
    }


    @Override
    public String getUsernameFromToken(String token) throws Exception {
        if (lp == null) {
            throw new Exception("User hashamp is null");
        }
        return (String) lp.get("username");
    }


    @Override
    public LocalDateTime getExpiryDateFromToken(String token) throws IOException {
        generateKey();
        Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
        return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }

    @Override
    public boolean validateToken(String token) throws Exception {
        generateKey();
        try {
            // https://stackoverflow.com/questions/65306718/io-jsonwebtoken-signatureexception-jwt-signature-does-not-match-locally-compute
            JwtParser jwt = Jwts.parser().setSigningKey(tokenSecret.getBytes(Charset.forName("UTF-8")));
            jwt.parseClaimsJws(token.replace("{", "").replace("}", "")).getBody();
            return true;
        } catch (SignatureException ex) {
            ex.printStackTrace();
        } catch (MalformedJwtException ex) {
            ex.printStackTrace();
        } catch (ExpiredJwtException ex) {
            ex.printStackTrace();
        } catch (UnsupportedJwtException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
