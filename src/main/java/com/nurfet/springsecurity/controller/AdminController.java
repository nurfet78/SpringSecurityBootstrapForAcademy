package com.nurfet.springsecurity.controller;

import com.nurfet.springsecurity.model.User;
import com.nurfet.springsecurity.service.AppService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AppService appService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(AppService appService, PasswordEncoder passwordEncoder) {
        this.appService = appService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String showUserList(@CurrentSecurityContext(expression = "authentication.principal") User principal, Model model) {
        model.addAttribute("user", principal);
        model.addAttribute("users", appService.findAllUsers());
        model.addAttribute("allRoles", appService.findAllRoles());

        return "user-list";
    }

    @GetMapping(value = "/new")
    public String addUserForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("allRoles", appService.findAllRoles());

        return "user-form";
    }

    @GetMapping("/{id}/newuser")
    public String editUserForm(@PathVariable(value = "id", required = true) Long userId, @ModelAttribute("user") User user, Model model) {
        try {
            model.addAttribute("allRoles", appService.findAllRoles());
            model.addAttribute("user", appService.findUser(userId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        return "user-form";
    }

    @PostMapping()
    public String addNewUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (!bindingResult.hasErrors()) {
            String oldPassword = user.getPassword();
            try {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                appService.insertUser(user);
            } catch (DataIntegrityViolationException e) {
                user.setPassword(oldPassword);
                addErrorIfDataIntegrityViolationException(bindingResult);
                addRedirectAttributesIfErrorsExists(user, bindingResult, redirectAttributes);
                return "redirect:/admin/new";
            }
        } else {
            addRedirectAttributesIfErrorsExists(user, bindingResult, redirectAttributes);
            return "redirect:/admin/new";
        }

        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long userId) {
        appService.deleteUser(userId);

        return "redirect:/admin";
    }


    @PutMapping("/{id}/update")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        bindingResult = checkBindingResultForPasswordField(bindingResult);

        if (!bindingResult.hasErrors()) {
            String oldPassword = user.getPassword();
            try {
                user.setPassword(user.getPassword().isEmpty() ?
                        appService.findUser(user.getId()).getPassword() :
                        passwordEncoder.encode(user.getPassword()));
                appService.updateUser(user);
            } catch (DataIntegrityViolationException e) {
                user.setPassword(oldPassword);
                addErrorIfDataIntegrityViolationException(bindingResult);
                addRedirectAttributesIfErrorsExists(user, bindingResult, redirectAttributes);
            }
        } else {
            addRedirectAttributesIfErrorsExists(user, bindingResult, redirectAttributes);
        }

        return "redirect:/admin";
    }


    private void addErrorIfDataIntegrityViolationException(BindingResult bindingResult) {
        bindingResult.addError(new FieldError(bindingResult.getObjectName(),
                "email", "E-mail must be unique"));
    }

    private void addRedirectAttributesIfErrorsExists(User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("user", user);
        redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
    }


    private BindingResult checkBindingResultForPasswordField(BindingResult bindingResult) {
        if (!bindingResult.hasFieldErrors()) {
            return bindingResult;
        }

        User user = (User) bindingResult.getTarget();
        BindingResult newBindingResult = new BeanPropertyBindingResult(user, bindingResult.getObjectName());
        for (FieldError error : bindingResult.getFieldErrors()) {
            if (!user.isNew() && !error.getField().equals("password")) {
                newBindingResult.addError(error);
            }
        }

        return newBindingResult;
    }

}
