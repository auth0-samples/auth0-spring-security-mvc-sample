package com.auth0.example.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
@Controller
public class RootController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    protected String Home(final HttpServletRequest req){
        return "login";
    }
}
