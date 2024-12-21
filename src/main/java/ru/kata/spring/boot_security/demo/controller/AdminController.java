package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String adminHomeAndAllUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("principal", principal);
        model.addAttribute("userPrincipal", userService.findByUsername(principal.getName()));
        return "users";
    }

    @PostMapping
    public String adminHomePost(@RequestParam("id") Long id, @RequestParam("action") String action) {
        User user = userService.findById(id);
        if (user != null) {
            if (action.equals("delete")) {
                userService.delete(user);
            } else if (action.equals("edit")) {
                return "redirect:/admin/update?id=" + user.getId();
            }
        }
        return "redirect:/admin";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "create";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user, @RequestParam("authorities") List<String> roles) {
        Set<Role> roleSet = new HashSet<>();
        if (roles.contains("ROLE_ADMIN")) {
            roleSet.addAll(roleService.findAll());
        } else {
            roleSet.add(roleService.findByName("ROLE_USER"));
        }
        user.setRoles(roleSet);
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String edit(Model model, @RequestParam("id") Long id) {
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("userRole", user.getAuthorities());
            model.addAttribute("isAdmin", user.getAuthorities().contains("ROLE_ADMIN"));
        }
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") User user, @RequestParam("id") Long id, @RequestParam("role") List<String> roles) {
        User updatedUser = userService.findById(id);
        if (updatedUser != null) {
            updatedUser.setUsername(user.getUsername());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setEmail(user.getEmail());

            Set<Role> roleSet = new HashSet<>();
            if (roles.contains("ROLE_ADMIN")) {
                roleSet.addAll(roleService.findAll());
            } else {
                roleSet.add(roleService.findByName("ROLE_USER"));
            }
            updatedUser.setRoles(roleSet);

            userService.update(updatedUser);
        }
        return "redirect:/admin";
    }
}

