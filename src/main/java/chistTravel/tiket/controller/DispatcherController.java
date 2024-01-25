package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.*;
import chistTravel.tiket.db.repository.UserRepository;
import chistTravel.tiket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/dispatcher")
public class DispatcherController {
    @Autowired
    private TravelService travelService;
    private LocalDateTime timeNow;
    @Autowired
    private UserJPAService userJPAService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RouteService routeService;
    @Autowired
    private RoleJPAService roleJPAService;
    @Autowired
    private LandingSiteService landingSiteService;

    @GetMapping("")
    private String showAllTravels(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        LocalDateTime today = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        LocalDateTime tomorrow = today.plusDays(1);
        LocalDateTime dayAfterTomorrow = today.plusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        model.addAttribute("today", formatter.format(today));
        model.addAttribute("tomorrow", formatter.format(tomorrow));
        model.addAttribute("dayAfterTomorrow", formatter.format(dayAfterTomorrow));
        return "dispatcher-day-list";
    }

    @GetMapping("/new/{id}")
    public String detailsTravels(@PathVariable("id") Long id, Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        Travels travels = travelService.findTravelsById(id);
        model.addAttribute("travels", travels);
        List<Route> usersInTravel = travels.getRoutes();
        model.addAttribute("usersInTravel", usersInTravel);

        List<Integer> listNumbers = new ArrayList<>();
        for (int i = 1; i <= usersInTravel.size(); i++) {
            listNumbers.add(i);
        }
        model.addAttribute("listNumbers", listNumbers);
        model.addAttribute("listNumbersSize", listNumbers.size());
        return "dispatcher-form";
    }

    @GetMapping("/order/{date}") // chistopol-kazan
    public String detailsTravelsForward(@PathVariable("date") String date, Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        String[] dateSplit = date.split(":");
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
        return "dispatcher-travels-list";
    }

    @GetMapping("/order/{date}/{id_order}")
    public String showNewRegistrationForm(@PathVariable("date") String date, @PathVariable("id_order") String id_order, Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        model.addAttribute("userJPA", new UserJPA());
        model.addAttribute("date", date);
        model.addAttribute("id_order", id_order);
        return "dispatcher-travels-details";
    }

    @PostMapping("/order/{date}/{id_order}")
    private String saveNewUserJPA(@PathVariable("date") String date, @PathVariable("id_order") String id_order, @ModelAttribute("userJPA") UserJPA user) {
        if (userJPAService.findByUsername(user.getUsername()) != null) {
            if (userJPAService.findByUsername(user.getUsername()).getUsername().equals(user.getUsername())) {
                String id_user = userJPAService.findByUsername(user.getUsername()).getId().toString();
                return "redirect:/dispatcher/order/{date}/{id_order}/" + id_user;
            }
        }
        ArrayList<RoleJPA> roles = new ArrayList<>();
        roles.add(roleJPAService.listAllRoles().get(1));
        user.setRolesJPA(roles);

        LocalDateTime time = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        user.setRegistered(formatter.format(time));
        user.setWrittenByDispatcher(true);
        userJPAService.saveUserJPA(user);
        String id_user = userJPAService.findByUsername(user.getUsername()).getId().toString();
        return "redirect:/dispatcher/order/{date}/{id_order}/" + id_user;
    }
    @GetMapping("/order/{date}/{id_order}/{id_user}")
    public String newOrder(@PathVariable("id_order") Long id_order, @PathVariable("date") String date, @PathVariable("id_user") String id_user, Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        model.addAttribute("user",userJPAService.findUserById(Long.valueOf(id_user)));
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
        Travels travel = travelService.findTravelsById(id_order);
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
        return "dispatcher-travels-details-2";
    }

    @PostMapping("/order/{date}/{id_order}/{id_user}")
    public String newOrderPost(@PathVariable("id_order") Long id_order, @PathVariable("date") String date, @PathVariable("id_user") String id_user, Route route, Principal principal, RedirectAttributes ra) {
        UserJPA user = userJPAService.findUserById(Long.valueOf(id_user));
        route.setStatus(true);
        timeNow = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        route.setRegistered(formatter.format(timeNow));
        route.setUser(user);
        Travels travel = travelService.findTravelsById(id_order);
        route.setTravel(travel);
        LocalTime time = LocalTime.parse(travel.getTimeParsed());
        LocalTime time1 = time.minusMinutes(Long.parseLong(route.getLandingSite().getTime()));
        route.setLandingTime(time1.toString());
        routeService.saveRoute(route);
        ra.addFlashAttribute("message", "Вы успешно записали на рейс " + user.getLastName());
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
