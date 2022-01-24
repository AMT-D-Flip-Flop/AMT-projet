/**
 * Date de création     : 06.12.2021
 * Groupe               : AMT-D-Flip-Flop
 * Description          : Implémentation de TokenProvider
 * Remarque             : -
 * Sources : TCMALTUNKAN - MEHMET ANIL ALTUNKAN
 */

package security;

import com.amt.dflipflop.Constants;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedHashMap;


@Service
public class TokenProviderImpl implements TokenProvider {
    Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private String tokenSecret = Constants.tokenSecretDefault;

    private boolean keyGenerated;


    //Define the key file for jwt
    private String jwtfileNamePath = Constants.jwtfileNamePath;

    //jwt token
    private String ROLE = "role";
    private String USERNAME = "username";
    private String CHARSET = "UTF-8";


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
        if (!keyGenerated && Constants.IS_PROD) {
            logger.error("reade file");
            tokenSecret = readLine(jwtfileNamePath);
            keyGenerated = true;
        }
    }


    @Override
    public HashMap setAccountFromToken(String token) throws Exception {
        generateKey();
        Claims claims = Jwts.parser().setSigningKey(tokenSecret.getBytes(Charset.forName(CHARSET))).parseClaimsJws(token).getBody();
        lp = new LinkedHashMap();
        lp.put(USERNAME, claims.getSubject());
        lp.put(ROLE, claims.get(ROLE));
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
        return (String) lp.get(USERNAME);
    }

    @Override
    public String getRoleFromToken(String token) throws Exception {
        if (lp == null) {
            throw new Exception("User hashamp is null");
        }
        return (String) lp.get(ROLE);
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
            JwtParser jwt = Jwts.parser().setSigningKey(tokenSecret.getBytes(Charset.forName(CHARSET)));
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
