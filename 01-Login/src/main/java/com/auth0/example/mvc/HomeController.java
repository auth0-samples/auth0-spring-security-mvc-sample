package com.auth0.example.mvc;

import com.auth0.example.security.TokenAuthentication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper mapper;

    public HomeController(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @RequestMapping(value = "/portal/home", method = RequestMethod.GET)
    protected String home(final Map<String, Object> model, final Principal principal) {
        logger.info("Home page");
        if (principal == null) {
            return "redirect:/logout";
        }
        TokenAuthentication tokenAuth = (TokenAuthentication) principal;
        String name = tokenAuth.getName();
        model.put("userId", tokenAuth.getName());
        try {
            Map<String, Object> userJsonMap = Maps.newHashMap();
            userJsonMap.put("appApiUserInfo", tokenAuth.getUserInfo().getValues());
            userJsonMap.put("managementApiUser", tokenAuth.getUser());
            model.put("userJson", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userJsonMap));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        model.put("provider", tokenAuth.getUser().getIdentities().get(0).getProvider());
        model.put("state", tokenAuth.getOAuthState().getState());
        return "home";
    }

}
