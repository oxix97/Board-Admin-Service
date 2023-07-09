package com.example.adminservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/management/user-accounts")
@Controller
public class UserAccountManagementController {
    @GetMapping
    public String managementAdmin() {
        return "management/user-accounts";
    }
}
