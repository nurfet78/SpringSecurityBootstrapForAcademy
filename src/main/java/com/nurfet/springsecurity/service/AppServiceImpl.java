package com.nurfet.springsecurity.service;

import com.nurfet.springsecurity.config.exception.LoginException;
import com.nurfet.springsecurity.model.Role;
import com.nurfet.springsecurity.model.User;
import com.nurfet.springsecurity.repository.RoleRepository;
import com.nurfet.springsecurity.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class AppServiceImpl implements AppService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    @Autowired
    public AppServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName", "lastName"));
    }

    @Override
    public User findUser(Long userId) throws IllegalArgumentException {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException(String.format("User with ID %d not found", userId)));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Iterable<Role> findAllRoles() {
        return roleRepository.findAll();
    }


    @Override
    public void insertUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findUsersByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Username %s not found", username))
        );
    }

    @Override
    public void authenticateOrLogout(Model model, HttpSession session,
                                     LoginException authenticationException,
                                     String authenticationName) {

        if (authenticationException != null) { // Восстанавливаем неверно введенные данные
            try {
                model.addAttribute("authenticationException", authenticationException);
                session.removeAttribute("Authentication-Exception");
            } catch (Exception e) {
                System.out.println("authenticationException: " + e);
            }
        } else {
            model.addAttribute("authenticationException", new LoginException(null));
        }

        if (authenticationName != null) { // Выводим прощальное сообщение
            try {
                model.addAttribute("authenticationName", authenticationName);
                session.removeAttribute("Authentication-Name");
            } catch (Exception e) {
                System.out.println("authenticationNameException: " + e);
            }
        }
    }
}
