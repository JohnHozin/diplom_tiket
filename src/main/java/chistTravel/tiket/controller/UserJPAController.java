package chistTravel.tiket.controller;


import chistTravel.tiket.db.entity.RoleJPA;
import chistTravel.tiket.db.entity.Route;
import chistTravel.tiket.db.entity.Travels;
import chistTravel.tiket.db.entity.UserJPA;
import chistTravel.tiket.service.RoleJPAService;
import chistTravel.tiket.service.UserJPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/registration")
public class UserJPAController {
    @Autowired
    private UserJPAService userJPAService;
    @Autowired
    private RoleJPAService roleJPAService;

    private LocalDateTime time;

    @GetMapping("")
    public String showNewRegistrationForm(Model model) {
        model.addAttribute("userJPA", new UserJPA());
        return "registration";
    }

    @PostMapping("")
    private String saveNewUserJPA(UserJPA user, RedirectAttributes ra) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        ArrayList<RoleJPA> roles = new ArrayList<>();
        roles.add(roleJPAService.listAllRoles().get(0));
        roles.add(roleJPAService.listAllRoles().get(1));
        roles.add(roleJPAService.listAllRoles().get(2));
        user.setRolesJPA(roles);

        time = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        user.setRegistered(formatter.format(time));

        UserJPA saved = userJPAService.saveUserJPA(user);
        ra.addFlashAttribute("message", "User " + saved + " saved successfully");
        return "redirect:/";
    }

    @GetMapping("/show")
    private String showAllUsers(Model model) {
        List<UserJPA> usersList = userJPAService.listAllUsers();

        for (UserJPA user: usersList) {

            List<Route> travels = user.getTravels();
            travels.sort(Comparator.comparing(Route::getLandingTime));
        }
        model.addAttribute("usersList", usersList);
        return "users-list";
    }
}
