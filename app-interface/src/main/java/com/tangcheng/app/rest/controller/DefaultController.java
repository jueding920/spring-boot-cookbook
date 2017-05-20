package com.tangcheng.app.rest.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by MyWorld on 2016/9/25.
 */
@Controller
public class DefaultController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);

    @Resource
    private Producer captchaProducer;

    @RequestMapping(value = {"/home", "/"}, method = RequestMethod.GET)
    public ModelAndView home() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("title", "Login success!");
        modelAndView.addObject("message", userDetails.getUsername());
        modelAndView.addObject("date", new Date());
        return modelAndView;
    }

    @RequestMapping("verification.jpg")
    public String verification(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        try (ServletOutputStream out = response.getOutputStream()) {
            ImageIO.write(captchaProducer.createImage(capText), "jpg", out);
            out.flush();
        }
        return null;
    }


}
