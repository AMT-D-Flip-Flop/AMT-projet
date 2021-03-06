/**
 * Date de création     : 06.12.2021
 * Dernier contributeur : Ryan Sauge
 * Groupe               : AMT-D-Flip-Flop
 * Description          : Interface pour gérer les tokens
 * Remarque             :
 * Sources :TCMALTUNKAN - MEHMET ANIL ALTUNKAN
 *
 */


package security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;


public interface TokenProvider {
    String getUsernameFromToken(String token) throws Exception;
    String getRoleFromToken(String token) throws Exception;

    HashMap setAccountFromToken(String token) throws Exception;

    LocalDateTime getExpiryDateFromToken(String token) throws IOException;

    boolean validateToken(String token) throws Exception;

}
