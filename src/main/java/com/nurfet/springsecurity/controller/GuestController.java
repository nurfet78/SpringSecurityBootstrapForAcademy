package com.nurfet.springsecurity.controller;

import com.nurfet.springsecurity.model.User;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guest")
public class GuestController {

    @GetMapping("")
    public String showGuestInfo(@CurrentSecurityContext(expression = "authentication.principal") User principal,
                                Model model) {
        model.addAttribute("guest", principal);

        return "guest-form";
    }
}
