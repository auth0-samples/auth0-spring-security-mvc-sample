package com.auth0.example;

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

    private AppConfig appConfig;

    @Autowired
    public ErrorController(final AppConfig appConfig) {
        super();
        this.appConfig = appConfig;
    }

    @RequestMapping("/error")
    protected String error(final RedirectAttributes redirectAttributes) throws IOException {
        logger.error("Handling error");
        final String logoutPath = appConfig.getOnLogoutRedirectTo();
        redirectAttributes.addFlashAttribute("error", true);
        return "redirect:" + logoutPath;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}
