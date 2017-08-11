package com.auth0.example.mvc;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.example.App;
import com.auth0.example.security.TokenAuthentication;
import com.auth0.json.auth.UserInfo;
import com.auth0.json.mgmt.users.User;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@SuppressWarnings("unused")
@Controller
public class CallbackController {


    @Autowired
    private AuthenticationController controller;
    @Autowired
    private ManagementAPI managementAPI;
    @Autowired
    private AuthAPI authAPI;
    @Autowired
    private App.OAuthStateRepo oAuthStateRepo;
    private final String redirectOnFail;
    private final String redirectOnSuccess;

    private final ObjectMapper mapper;

    public CallbackController(ObjectMapper mapper) {
        this.mapper = mapper;
        this.redirectOnFail = "/login";
        this.redirectOnSuccess = "/portal/home";
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    protected void getCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        handle(req, res);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    protected void postCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        handle(req, res);
    }

    private void handle(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String state = req.getParameter("state");
            App.OAuthState oAuthState = oAuthStateRepo.getOAuthState(state);
            System.out.println("\n\n\n\n received state = " + state + "\n\n\n\n");
            Tokens tokens = controller.handle(req);
            Map<String, String> tokenVals = Maps.newHashMap();
            tokenVals.put("accessToken", tokens.getAccessToken());
            tokenVals.put("idToken", tokens.getIdToken());
            tokenVals.put("refreshToken", tokens.getRefreshToken());
            tokenVals.put("type", tokens.getType());
            tokenVals.put("expires", tokens.getExpiresIn() + "");
            String tokenValues = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tokenVals);
            System.out.println("tokenValues = " + tokenValues);
            UserInfo userInfo = authAPI.userInfo(tokens.getAccessToken()).execute();
            User user = managementAPI.users().get(userInfo.getValues().get("sub").toString(), null).execute();
            String userValue = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
            System.out.println("userValue = " + userValue);
            String userValues = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userInfo.getValues());
            System.out.println("userValues = " + userValues);
            TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokens.getIdToken()), userInfo, user, oAuthState);
            SecurityContextHolder.getContext().setAuthentication(tokenAuth);
            res.sendRedirect(redirectOnSuccess);
        } catch (AuthenticationException | IdentityVerificationException e) {
            e.printStackTrace();
            SecurityContextHolder.clearContext();
            res.sendRedirect(redirectOnFail);
        }
    }

}
