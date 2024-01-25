package chistTravel.tiket.controller;


import chistTravel.tiket.db.entity.Bus;
import chistTravel.tiket.db.entity.RoleJPA;
import chistTravel.tiket.db.entity.Route;
import chistTravel.tiket.db.entity.UserJPA;
import chistTravel.tiket.db.repository.UserRepository;
import chistTravel.tiket.service.RoleJPAService;
import chistTravel.tiket.service.UserJPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("")
public class UserJPAController {
    @Autowired
    private UserJPAService userJPAService;
    @Autowired
    private RoleJPAService roleJPAService;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/registration")
    public String showNewRegistrationForm(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        model.addAttribute("userJPA", new UserJPA());
        return "registration";
    }

    @PostMapping("/registration")
    private String saveNewUserJPA(@ModelAttribute("userJPA") UserJPA user, Model model, RedirectAttributes ra) {
        UserJPA newUser = userJPAService.findByUsername(user.getUsername());
        if (newUser != null) {
            if (newUser.getUsername().equals(user.getUsername())) {
                if (newUser.isWrittenByDispatcher()) {
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
                    String hashedPassword = passwordEncoder.encode(user.getPassword());
                    user.setPassword(hashedPassword);
                    ArrayList<RoleJPA> roles = new ArrayList<>();
                    roles.add(roleJPAService.listAllRoles().get(1));
                    user.setRolesJPA(roles);

                    LocalDateTime time = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                    user.setRegistered(formatter.format(time));
                    userJPAService.updateUserJPA(newUser.getId(),user);
                    ra.addFlashAttribute("message", "Аккаунт успешно создан!");
                } else {
                    model.addAttribute("usernameError", "Пользователь с таким телефоном уже существует");
                    return "registration";
                }
            }
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        ArrayList<RoleJPA> roles = new ArrayList<>();
        roles.add(roleJPAService.listAllRoles().get(1));
        user.setRolesJPA(roles);

        LocalDateTime time = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        user.setRegistered(formatter.format(time));
        user.setWrittenByDispatcher(false);
        userJPAService.saveUserJPA(user);
        ra.addFlashAttribute("message", "Аккаунт успешно создан");
        return "redirect:/";
    }

    @GetMapping("/account")
    public String personalAccount(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserJPA user = userRepository.findByUsername(authenticationName);
        List<Route> travels = user.getTravels();
        travels.sort(Comparator.comparing(Route::getLandingTime));
        model.addAttribute("user", user);
        model.addAttribute("travels", travels);

        LocalDateTime today = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        LocalDateTime tomorrow = today.plusDays(1);
        LocalDateTime dayAfterTomorrow = today.plusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        model.addAttribute("today",formatter.format(today));
        model.addAttribute("tomorrow",formatter.format(tomorrow));
        model.addAttribute("dayAfterTomorrow",formatter.format(dayAfterTomorrow));
        model.addAttribute("exit","yes");
        return "personal-account";
    }

    @GetMapping("/users")
    private String showAllUsers(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        List<UserJPA> usersList = userJPAService.listAllUsers();
//        for (UserJPA user: usersList) {
//            List<Route> travels = user.getTravels();
//            travels.sort(Comparator.comparing(Route::getLandingTime));
//        }
        model.addAttribute("usersList", usersList);
        return "users-list";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
        userJPAService.deleteUserById(id);
        ra.addFlashAttribute("message", "Пользователь удалён");
        return "redirect:/users";
    }

    @GetMapping("/users/details/{id}")
    public String detailsUser(@PathVariable("id") Long id, Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        UserJPA user = userJPAService.findUserById(id);
        List<Route> travels = user.getTravels();
        travels.sort(Comparator.comparing(Route::getLandingTime));
        model.addAttribute("user", user);
        model.addAttribute("travels", travels);

        LocalDateTime today = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        LocalDateTime tomorrow = today.plusDays(1);
        LocalDateTime dayAfterTomorrow = today.plusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        model.addAttribute("today",formatter.format(today));
        model.addAttribute("tomorrow",formatter.format(tomorrow));
        model.addAttribute("dayAfterTomorrow",formatter.format(dayAfterTomorrow));
        model.addAttribute("exit","no");
        return "personal-account";
    }

    public String getRoles() {
        String role = "";
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!authenticationName.equals("anonymousUser")) {
            UserJPA user = userRepository.findByUsername(authenticationName);
            Collection<RoleJPA> roleJPAS = user.getRolesJPA();
            List<RoleJPA> roleJPASAll = roleJPAService.listAllRoles();
            if (roleJPAS.contains(roleJPASAll.get(0))) {
                role = "ADMIN";
            } else if (roleJPAS.contains(roleJPASAll.get(2))) {
                role = "DISPATCHER";
            } else if (roleJPAS.contains(roleJPASAll.get(3))) {
                role = "DRIVER";
            } else if (roleJPAS.contains(roleJPASAll.get(1))) {
                role = "USER";
            }
        }
        return role;
    }
}
