package com.example.adminservice.controller;

import com.example.adminservice.dto.response.UserAccountResponse;
import com.example.adminservice.service.UserAccountManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/management/user-accounts")
@RequiredArgsConstructor
@Controller
public class UserAccountManagementController {
    private final UserAccountManagementService service;

    @GetMapping
    public String users(Model model) {
        model.addAttribute(
                "userAccounts",
                service.getUserAccounts().stream()
                        .map(UserAccountResponse::from)
                        .toList()
        );

        return "management/user-accounts";
    }

    @ResponseBody
    @GetMapping("/{id}")
    public UserAccountResponse getUser(@PathVariable String id) {
        return UserAccountResponse.from(service.getUserAccount(id));
    }

    @PostMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        service.deleteUserAccount(id);
        return "redirect:/management/user-accounts";
    }
}
