package com.auth0.example.mvc;

import com.auth0.example.security.AppConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private AppConfig appConfig;

    @Override
    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse res,
                       Authentication authentication) {
        invalidateSession(req);
        String returnTo = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
        String logoutUrl = String.format(
                "https://%s/v2/logout?client_id=%s&returnTo=%s",
                appConfig.getDomain(),
                appConfig.getClientId(),
                returnTo);
        try {
            res.sendRedirect(logoutUrl);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void invalidateSession(HttpServletRequest request) {
        if (request.getSession() != null) {
            request.getSession().invalidate();
        }
    }
}