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
    private String mode = "prod";
    //private String mode = "noProd";
    private String jwtfileNamePath = "/opt/tomcat/webapps/zone_secret/jwt.txt";

    @Value("${authentication-test.auth.tokenExpirationMsec}")
    private Long tokenExpirationMsec;

    @Value("${authentication-test.auth.refreshTokenExpirationMsec}")
    private Long refreshTokenExpirationMsec;

    private LinkedHashMap lp;

    public TokenProviderImpl() {
        this.keyGenerated = false;
    }


    //Read file content into the string with - Files.lines(Path path, Charset cs)

    private static String readLine(String filePath) throws IOException {
        // File path is passed as parameter
        File file = new File(
                filePath);

        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)

        // Creating an object of BufferedReader class
        BufferedReader br
                = new BufferedReader(new FileReader(file));

        // Declaring a string variable
        String st = br.readLine();
        // Consition holds true till
        // there is character in a string

        return st;
    }

    void generateKey() throws IOException {
        //logger.error("key generate");
        //logger.error("value:" + keyGenerated);
        // logger.error("value:" + mode.equals("prod"));

        if (!keyGenerated && mode.equals("prod")) {
            logger.error("reade file");
            tokenSecret = readLine(jwtfileNamePath);
            keyGenerated = true;
        }
    }

    @Override
    public Token generateAccessToken(String subject) throws IOException {
        generateKey();
        Date now = new Date();
        Long duration = now.getTime() + tokenExpirationMsec;
        Date expiryDate = new Date(duration);
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
        return new Token(Token.TokenType.ACCESS, token, duration, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public Token generateRefreshToken(String subject) throws IOException {
        generateKey();
        Date now = new Date();
        Long duration = now.getTime() + refreshTokenExpirationMsec;
        Date expiryDate = new Date(duration);
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
        return new Token(Token.TokenType.REFRESH, token, duration, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public HashMap getAccountFromToken(String token) throws Exception {
        generateKey();
        Claims claims = Jwts.parser().setSigningKey(tokenSecret.getBytes(Charset.forName("UTF-8"))).parseClaimsJws(token).getBody();
        logger.error(claims.toString());
        lp = new LinkedHashMap();
        lp.put("username", claims.getSubject());
        lp.put("role", claims.get("role"));
        // lp = (LinkedHashMap) claims.get("account");
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
        //return (String) lp.get("username");
        //return claims.get("account").;

        //throw new Exception("d" + claims.getSubject() + claims.getExpiration() + claims.get("account") + claims.get("Account"));
        //return claims.getSubject();

    }


    @Override
    public LocalDateTime getExpiryDateFromToken(String token) throws IOException {
        generateKey();
        Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
        return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
    }

    @Override
    public boolean validateToken(String token) throws Exception {
        return true;
        /*generateKey();
        try {
           // https://stackoverflow.com/questions/65306718/io-jsonwebtoken-signatureexception-jwt-signature-does-not-match-locally-compute

            JwtParser jwt = Jwts.parser().setSigningKey(tokenSecret.getBytes(Charset.forName("UTF-8")));
            jwt.parseClaimsJws(token.replace("{", "").replace("}", "")).getBody();
            //jwt.parse(token);
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
        return false;*/
    }
}
