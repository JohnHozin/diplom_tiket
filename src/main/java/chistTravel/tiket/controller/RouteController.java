package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.*;
import chistTravel.tiket.db.repository.UserRepository;
import chistTravel.tiket.service.RoleJPAService;
import chistTravel.tiket.service.RouteService;
import chistTravel.tiket.service.TravelService;
import chistTravel.tiket.service.UserJPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/routes")
public class RouteController {
    @Autowired
    private RouteService routeService;
    @Autowired
    private UserJPAService userJPAService;
    @Autowired
    private TravelService travelService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleJPAService roleJPAService;

    @GetMapping("")
    private String showAllTravelLists(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        List<Route> routeList = routeService.listAllRoute();
        model.addAttribute("routeList", routeList);
        return "routes-list";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        model.addAttribute("route", new Route());
        List<UserJPA> userList = userJPAService.listAllUsers();
        model.addAttribute("userList", userList);
        List<Travels> travelsLists = travelService.listAllTravels();
        model.addAttribute("travelsLists", travelsLists);
        return "route-form";
    }

    @PostMapping("/new")
    private String saveNewUser(Route route, RedirectAttributes ra) {
        route.setStatus(true);
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        route.setRegistered(formatter.format(time));
        Route saved = routeService.saveRoute(route);
        ra.addFlashAttribute("message", "Route " + saved + " saved successfully");
        return "redirect:/routes";
    }

    @GetMapping("/deleteFull/{id}")
    public String deleteRoute(@PathVariable("id") Long id, RedirectAttributes ra) {
        routeService.deleteRouteById(id);
        ra.addFlashAttribute("message", "Route deleted");
        return "redirect:/routes";
    }

    @GetMapping("/delete/{id}")
    public String statusToFalseRoute(@PathVariable("id") Long id, RedirectAttributes ra) {
        routeService.statusToFalseRouteById(id);
        ra.addFlashAttribute("message", "Route deleted by status");
        return "redirect:/routes";
    }

    @GetMapping("/repair/{id}")
    public String statusToTrueRoute(@PathVariable("id") Long id, RedirectAttributes ra, Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        routeService.statusToTrueRouteById(id);
        ra.addFlashAttribute("message", "Route repair by status");
        return "redirect:/routes";
    }

    @GetMapping("/dispatcher1")
    private String showAllTravelListsToDispatcher(Model model, Principal principal) {
        String role = getRoles();
        model.addAttribute("role", role);
        List<Route> routeList = routeService.listAllRoute();
        List<Route> routeList2 = new ArrayList<>();
        for (int i = 0; i < routeList.size(); i++) {
            if (routeList.get(i).getStatus().equals(true)){
                routeList2.add(routeList.get(i));
            }
        }
        model.addAttribute("routeList", routeList2);
        model.addAttribute("userJPA",userJPAService.findByUsername(principal.getName()));
        return "routes-list-dispatcher";
    }

    @GetMapping("/dispatcher/delete/{id}")
    public String statusToFalseRouteDispatcher(@PathVariable("id") Long id, RedirectAttributes ra) {
        routeService.statusToFalseRouteById(id);
        ra.addFlashAttribute("message", "Route deleted by status");
        return "redirect:/routes/dispatcher";
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
