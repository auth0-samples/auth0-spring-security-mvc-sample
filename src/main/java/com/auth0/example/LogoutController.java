package com.auth0.example;

import com.auth0.web.Auth0Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LogoutController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Auth0Config auth0Config;

    @Autowired
    public LogoutController(Auth0Config auth0Config) {
        this.auth0Config = auth0Config;
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    protected String logout(final HttpServletRequest request) {
        logger.debug("Performing logout");
        invalidateSession(request);
        final String logoutPath = auth0Config.getOnLogoutRedirectTo();
        return "redirect:" + logoutPath;
    }

    private void invalidateSession(HttpServletRequest request) {
        if (request.getSession() != null) {
            request.getSession().invalidate();
        }
    }

}
