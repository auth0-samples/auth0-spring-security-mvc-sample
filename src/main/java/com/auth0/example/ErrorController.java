package com.auth0.example;

import com.auth0.web.Auth0Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PATH = "/error";

    private Auth0Config auth0Config;

    @Autowired
    public ErrorController(Auth0Config auth0Config) {
        super();
        this.auth0Config = auth0Config;
    }

    @RequestMapping("/error")
    protected String error(final RedirectAttributes redirectAttributes) throws IOException {
        logger.error("Handling error");
        final String logoutPath = auth0Config.getOnLogoutRedirectTo();
        redirectAttributes.addFlashAttribute("error", true);
        return "redirect:" + logoutPath;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}
