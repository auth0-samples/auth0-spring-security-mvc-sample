package com.auth0.example.security;

import com.auth0.example.App;
import com.auth0.json.auth.UserInfo;
import com.auth0.json.mgmt.users.User;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Lists;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private final DecodedJWT jwt;
    private final UserInfo userInfo;
    private final User user;
    private final App.OAuthState oAuthState;
    private boolean invalidated;

    public TokenAuthentication(DecodedJWT jwt, UserInfo userInfo, User user, App.OAuthState oAuthState) {
        super(readAuthorities(jwt));
        this.jwt = jwt;
        this.userInfo = userInfo;
        this.user = user;
        this.oAuthState = oAuthState;
    }

    private boolean hasExpired() {
        return jwt.getExpiresAt().before(new Date());
    }

    private static Collection<? extends GrantedAuthority> readAuthorities(DecodedJWT jwt) {
        Claim rolesClaim = jwt.getClaim("https://access.control/roles");
        if (rolesClaim.isNull()) {
            return Collections.emptyList();
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] scopes = rolesClaim.asArray(String.class);
        for (String s : scopes) {
            SimpleGrantedAuthority a = new SimpleGrantedAuthority(s);
            if (!authorities.contains(a)) {
                authorities.add(a);
            }
        }
        return authorities;
    }


    @Override
    public String getCredentials() {
        return jwt.getToken();
    }

    private List<String> nameKeys = Lists.newArrayList("name", "displayName", "userPrincipalName");

    @Override
    public String getName() {
        for (String key : nameKeys) {
            Optional<String> oName = Optional.ofNullable(userInfo.getValues().get(key))
                    .map(Object::toString)
                    .filter(name -> name.length() > 0);
            if (oName.isPresent()) {
               return oName.get();
            }
        }
        return "unknown";
    }

    @Override
    public Object getPrincipal() {
        return jwt.getSubject();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public User getUser() {
        return user;
    }

    public App.OAuthState getOAuthState() {
        return oAuthState;
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
}
