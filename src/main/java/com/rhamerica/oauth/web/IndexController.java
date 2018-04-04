package com.rhamerica.oauth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @RequestMapping("/")
    public ModelAndView modelAndView() {
        ModelAndView mav = new ModelAndView("tpl/home");
        return mav;
    }
}
