package com.kt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @program: potato
 * @description
 * @author: Tan.
 * @create: 2020-11-11 17:11
 **/
@RestController
public class HellController {

    private final static Logger logger = LoggerFactory.getLogger(HellController.class);

    @GetMapping("/hello")
    public Object hello() {
        logger.info("hello");
        return "hello World";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new user");
        session.setMaxInactiveInterval(3600);
        session.getAttribute("userInfo");
        return "ok";
    }

}
