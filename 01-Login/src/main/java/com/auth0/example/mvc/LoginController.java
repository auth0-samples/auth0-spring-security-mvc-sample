package com.auth0.example.mvc;

import com.auth0.AuthenticationController;
import com.auth0.AuthorizeUrl;
import com.auth0.example.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
@Controller
public class LoginController {

    @Autowired private AuthenticationController controller;
    @Autowired private App.OAuthStateRepo oAuthStateRepo;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    protected String login(final HttpServletRequest req) {
        logger.debug("Performing login");
        String redirectUri = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/callback";
        AuthorizeUrl authorizeUrl = controller.buildAuthorizeUrl(req, redirectUri);
        authorizeUrl.withScope("openid profile email read:user_idp_tokens");
        App.OAuthState oAuthState = App.OAuthState.withTenant("myTenant");
        oAuthStateRepo.initializeOAuthState(oAuthState);
        authorizeUrl.withState(oAuthState.getState());
        return "redirect:" + authorizeUrl.build();
    }

}
