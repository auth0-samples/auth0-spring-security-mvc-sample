package com.auth0.example.mvc;

import com.auth0.example.security.TokenAuthentication;
import com.auth0.example.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SuppressWarnings("unused")
@Controller
public class ProfileController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    protected String profile(final Model model, final Authentication authentication) {

        // Since we've configured Spring Security to only allow authenticated requests to
        // reach this endpoint, and we control the Authentication implementation, we can safely cast.
        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        if (tokenAuthentication == null) {
            return "redirect:/login";
        }

        String profileJson = TokenUtils.claimsAsJson(tokenAuthentication.getClaims());

        model.addAttribute("profile", tokenAuthentication.getClaims());
        model.addAttribute("profileJson", profileJson);
        return "profile";
    }

}
