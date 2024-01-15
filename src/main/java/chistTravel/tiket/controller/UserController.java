package chistTravel.tiket.controller;


import chistTravel.tiket.db.entity.RoleJPA;
import chistTravel.tiket.db.entity.UserJPA;
import chistTravel.tiket.service.RoleJPAService;
//import chistTravel.tiket.service.RoleService;
import chistTravel.tiket.service.UserJPAService;
import chistTravel.tiket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleJPAService roleService;

    private LocalDateTime time;

    @GetMapping("")
    private String showAllUsers(Model model) {
        List<UserJPA> usersList = userService.listAllUsers();
        model.addAttribute("usersList", usersList);
        return "users-list";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new UserJPA());
        return "user-form";
    }

    @PostMapping("/new")
    private String saveNewUser(UserJPA user, RedirectAttributes ra) {
        List<RoleJPA> roles = roleService.listAllRoles();
//        user.setRole(roles.get(3));

        time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        user.setRegistered(formatter.format(time));

        UserJPA saved = userService.saveUser(user);
        ra.addFlashAttribute("message", "User " + saved + " saved successfully");
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
        userService.deleteUserById(id);
        ra.addFlashAttribute("message", "User deleted");
        return "redirect:/users";
    }

    @GetMapping("/details/{id}")
    public String detailsUser(@PathVariable("id") Long id, Model model) {
        UserJPA user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "user-details";
    }
}
