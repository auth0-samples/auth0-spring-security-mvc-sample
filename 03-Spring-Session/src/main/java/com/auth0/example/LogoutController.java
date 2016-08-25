package com.auth0.example;

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

    private AppConfig appConfig;

    @Autowired
    public LogoutController(final AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    protected String logout(final HttpServletRequest request) {
        logger.debug("Performing logout");
        invalidateSession(request);
        final String logoutPath = appConfig.getOnLogoutRedirectTo();
        return "redirect:" + logoutPath;
    }

    private void invalidateSession(HttpServletRequest request) {
        if (request.getSession() != null) {
            request.getSession().invalidate();
        }
    }

}
