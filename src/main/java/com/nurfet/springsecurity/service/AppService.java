package com.nurfet.springsecurity.service;

import com.nurfet.springsecurity.config.exception.LoginException;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.nurfet.springsecurity.model.User;
import com.nurfet.springsecurity.model.Role;
import org.springframework.ui.Model;

import java.util.List;

public interface AppService extends UserDetailsService {

    List<User> findAllUsers();

    User findUser(Long userId) throws IllegalArgumentException;

    void deleteUser(Long userId);

    Iterable<Role> findAllRoles();

    void authenticateOrLogout(Model model, HttpSession session, LoginException authenticationException,
                              String authenticationName);

    void insertUser(User user);

    void updateUser(User user);

}
