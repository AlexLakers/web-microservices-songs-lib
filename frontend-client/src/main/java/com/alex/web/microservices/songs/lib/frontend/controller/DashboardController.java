package com.alex.web.microservices.songs.lib.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This is the main controller for visualisations all the information from different services.
 * It represents dashboard.html
 */
@RequestMapping("/dashboard")
@Controller
public class DashboardController {
    @GetMapping
    public String dashboard(){
        return "dashboard";
    }
}
