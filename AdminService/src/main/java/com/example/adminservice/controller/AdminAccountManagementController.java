package com.example.adminservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/management/admin")
@Controller
public class AdminAccountManagementController {
    @GetMapping
    public String managementAdmin() {
        return "management/admin";
    }
}
