package com.glovoapp.backender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
//@ComponentScan()
//@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = "com.glovoapp.backender")
class API
{
    private final String welcomeMessage;

    @Autowired
    API(@Value("${backender.welcome_message}") final String welcomeMessage)
    {
        this.welcomeMessage = welcomeMessage;
    }

    @RequestMapping("/")
    @ResponseBody
    String root()
    {
        return welcomeMessage;
    }

    public static void main(final String[] args)
    {
        SpringApplication.run(API.class);
    }
}
