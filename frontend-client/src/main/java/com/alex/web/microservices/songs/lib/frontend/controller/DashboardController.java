package com.alex.web.microservices.songs.lib.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/dashboard")
@Controller
public class DashboardController {
    @GetMapping
    public String dashboard(){
        return "dashboard";
    }
}
