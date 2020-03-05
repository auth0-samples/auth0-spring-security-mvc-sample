package com.auth0.example.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private final DecodedJWT jwt;
    private boolean invalidated;

    public TokenAuthentication(DecodedJWT jwt) {
        // No authorities by default for this login example. You can use the DecodedJWT to create authorities from
        // claims as needed for your particular use case.
        super(null);
        this.jwt = jwt;
    }

    private boolean hasExpired() {
        return jwt.getExpiresAt().before(new Date());
    }

    @Override
    public String getCredentials() {
        return jwt.getToken();
    }

    @Override
    public Object getPrincipal() {
        return jwt.getSubject();
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Create a new Authentication object to authenticate");
        }
        invalidated = true;
    }

    @Override
    public boolean isAuthenticated() {
        return !invalidated && !hasExpired();
    }

    /**
     * Gets the claims for this JWT token.
     * <br>
     * For an ID token, claims represent user profile information such as the user's name, profile, picture, etc.
     * <br>
     * @see <a href="https://auth0.com/docs/tokens/id-token">ID Token Documentation</a>
     * @return a Map containing the claims of the token.
     */
    public Map<String, Claim> getClaims() {
        return jwt.getClaims();
    }

}
