/**
 * Date de création     : 06.12.2021
 * Dernier contributeur : Ryan Sauge
 * Groupe               : AMT-D-Flip-Flop
 * Description          : Filter pour récupérer et tester le jwt
 * Remarque             : -
 * Sources :
 */

package security;


import com.amt.dflipflop.Entities.authentification.CustomAuthenticationProvider;
import com.amt.dflipflop.Entities.authentification.UserJson;
import com.amt.dflipflop.Services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    //Value("${authentication-test.auth.accessTokenCookieName}")
    private AuthenticationManager authenticationManager = new AuthenticationManager() {
        CustomAuthenticationProvider cp = new CustomAuthenticationProvider();
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            return cp.authenticate(authentication);
        }
    };
    private String accessTokenCookieName ="bearer";

    @Value("${authentication-test.auth.refreshTokenCookieName}")
    private String refreshTokenCookieName;


    private TokenProvider tokenProvider = new TokenProviderImpl();

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtToken(httpServletRequest, true);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                tokenProvider.getAccountFromToken(jwt);
                String username = tokenProvider.getUsernameFromToken(jwt);
                UserJson userJsonResponse = new UserJson();
                userJsonResponse.setUsername(username);
                UsernamePasswordAuthenticationToken authRequest
                        = new UsernamePasswordAuthenticationToken(username, "");

                // Authenticate the user
                Authentication authentication = authenticationManager.authenticate(authRequest);
                SecurityContext securityContext = SecurityContextHolder.getContext();

                securityContext.setAuthentication(authentication);
                //userJsonResponse.setAccountPublic();
                //UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                //UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                //SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);
            if (accessToken == null) return null;

            return accessToken;
        }
        return null;
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        //Null check necessery because boucle for each
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if (accessTokenCookieName.equals(cookie.getName())) {
                    String accessToken = cookie.getValue();
                    if (accessToken == null) return null;

                    return accessToken;
                }
            }
        }
        return null;
    }

    private String getJwtToken(HttpServletRequest request, boolean fromCookie) {
        if (fromCookie) return getJwtFromCookie(request);

        return getJwtFromRequest(request);
    }


}
