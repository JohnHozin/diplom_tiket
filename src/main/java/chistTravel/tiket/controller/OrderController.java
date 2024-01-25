package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.*;
import chistTravel.tiket.db.repository.RoleRepository;
import chistTravel.tiket.db.repository.UserRepository;
import chistTravel.tiket.service.*;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private TravelService travelService;
    private LocalDateTime timeNow;
    @Autowired
    private UserJPAService userJPAService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleJPAService roleJPAService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private LandingSiteService landingSiteService;

    @GetMapping("")
    private String showOrder(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        List<Travels> travelsLists = travelService.listAllTravels();
        model.addAttribute("travelsLists", travelsLists);
        return "travels-list";
    }

    @GetMapping("/{date}") // chistopol-kazan
    public String detailsTravelsForward(@PathVariable("date") String date, Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        String [] dateSplit = date.split(":");
        String dateParsed = dateSplit[1];
        String direction = dateSplit[0];
        List<Travels> travelsDateParsed = new ArrayList<>();
        if (direction.equals("chistopol-kazan")) {
            travelsDateParsed = travelService.findTravelsByDateForward(dateParsed);
            model.addAttribute("directionName", "Чистополь - Казань");
        } else if (direction.equals("kazan-chistopol")) {
            travelsDateParsed = travelService.findTravelsByDateBack(dateParsed);
            model.addAttribute("directionName", "Казань - Чистополь");
        } else {
            model.addAttribute("directionName", "Ошибка направления");
        }
        timeNow = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        LocalTime timeHourPlus = LocalTime.from(timeNow).plusHours(1);
        int dateDay = LocalDate.from(timeNow).getDayOfYear();
        List<Travels> travelsValid = new ArrayList<>();
        List<Travels> travelsInvalid = new ArrayList<>();
        for (Travels travel : travelsDateParsed) {
            LocalTime time = LocalTime.parse(travel.getTimeParsed());
            int localDate = LocalDateTime.parse(travel.getDateFull()).getDayOfYear();
            // рейсы через час
            if (timeHourPlus.isBefore(time) && localDate == dateDay && timeHourPlus.getHour() >= 1 || localDate > dateDay && localDate < dateDay + 3) {
                travelsValid.add(travel);
            } else {
                travelsInvalid.add(travel);
            }
        }
        model.addAttribute("travelsDateParsed", travelsDateParsed);
        model.addAttribute("travelsValid", travelsValid);
        model.addAttribute("travelsInvalid", travelsInvalid);
        model.addAttribute("dateParsed", dateParsed);
        model.addAttribute("direction", direction);
        return "order-list";
    }

    @GetMapping("/{date}/{id}")
    public String detailsOrder(@PathVariable("id") Long id, @PathVariable("date") String date, Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        String [] dateSplit = date.split(":");
        String dateParsed = dateSplit[1];
        String direction = dateSplit[0];
        model.addAttribute("dateParsed", dateParsed);
        model.addAttribute("direction", direction);
        if (direction.equals("chistopol-kazan")) {
            model.addAttribute("directionName", "Чистополь - Казань");
        } else if (direction.equals("kazan-chistopol")) {
            model.addAttribute("directionName", "Казань - Чистополь");
        } else {
            model.addAttribute("directionName", "Ошибка направления");
        }
        Travels travel = travelService.findTravelsById(id);
        model.addAttribute("travels", travel);
        List<Route> usersInTravel = travel.getRoutes();
        usersInTravel.sort(Comparator.comparing(Route::getLandingTime));
        model.addAttribute("usersInTravel", usersInTravel);
        model.addAttribute("route", new Route());
        List<LandingSite> landingSites = landingSiteService.listAllLandingSites();
        LocalTime time = LocalTime.parse(travel.getTimeParsed());
        List<LandingSite> landingSitesDirection= new ArrayList<>();
        for (LandingSite landingSite : landingSites) {
            if (direction.equals("chistopol-kazan") && landingSite.isDirection()) {
                LocalTime time1 = time.minusMinutes(Long.parseLong(landingSite.getTime()));
                landingSite.setTime(time1.toString());
                landingSitesDirection.add(landingSite);
            } else if (direction.equals("kazan-chistopol") && !landingSite.isDirection()) {
                LocalTime time1 = time.minusMinutes(Long.parseLong(landingSite.getTime()));
                landingSite.setTime(time1.toString());
                landingSitesDirection.add(landingSite);
            }
        }
        landingSitesDirection.sort(Comparator.comparing(LandingSite::getTime));
        model.addAttribute("landingSites", landingSitesDirection);
        return "order-details";
    }

    @PostMapping ("/{date}/{id_travel}")
    public String detailsOrderPost(@PathVariable("id_travel") Long id_travel, @PathVariable("date") String date, Route route, Principal principal, RedirectAttributes ra) {
        route.setStatus(true);
        timeNow = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        route.setRegistered(formatter.format(timeNow));
        route.setUser(userJPAService.findByUsername(principal.getName()));
        Travels travel = travelService.findTravelsById(id_travel);
        route.setTravel(travel);
        LocalTime time = LocalTime.parse(travel.getTimeParsed());
        LocalTime time1 = time.minusMinutes(Long.parseLong(route.getLandingSite().getTime()));
        route.setLandingTime(time1.toString());
        routeService.saveRoute(route);
        ra.addFlashAttribute("message", "Вы успешно записались на рейс");
        return "redirect:/order/{date}";
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
