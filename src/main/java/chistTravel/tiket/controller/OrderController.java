package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.*;
import chistTravel.tiket.service.LandingSiteService;
import chistTravel.tiket.service.RouteService;
import chistTravel.tiket.service.TravelService;
import chistTravel.tiket.service.UserJPAService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private RouteService routeService;
    @Autowired
    private LandingSiteService landingSiteService;

    @GetMapping("")
    private String showOrder(Model model) {
        List<Travels> travelsLists = travelService.listAllTravels();
        model.addAttribute("travelsLists", travelsLists);
        return "travels-list";
    }

    @GetMapping("/{date}") // chistopol-kazan
    public String detailsTravelsForward(@PathVariable("date") String date, Model model) {
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
//        travelsForDate.sort(Comparator.comparing(Travels::getTimeParsed));
//        List travelsDateParsedDisabled = travelService.findTravelsByDateDisabled(dateParsed);
        model.addAttribute("travelsDateParsed", travelsDateParsed);
        model.addAttribute("travelsValid", travelsValid);
        model.addAttribute("travelsInvalid", travelsInvalid);
        model.addAttribute("dateParsed", dateParsed);
        model.addAttribute("direction", direction);
//        Set<Route> usersInTravel = travels.getRoutes();
//        model.addAttribute("usersInTravel", usersInTravel);

//        List<Integer> listNumbers = new ArrayList<>();
//        for (int i =1; i <=usersInTravel.size(); i ++){
//            listNumbers.add(i);
//        }
//        model.addAttribute("listNumbers", listNumbers);
        return "order-list";
    }

    @GetMapping("/{date}/{id}")
    public String detailsOrder(@PathVariable("id") Long id, @PathVariable("date") String date, Model model) {
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
        // перенести эту логику в форму записи
//        LocalTime time = LocalTime.parse(travel.getTimeParsed());
//        for (Route route : usersInTravel) {
//            LocalTime time1 = time.minusMinutes(Long.parseLong(route.getLandingSite().getTime()));
//            route.setLandingTime(time1.toString());
//        }
        // !!!
        usersInTravel.sort(Comparator.comparing(Route::getLandingTime));
        model.addAttribute("usersInTravel", usersInTravel);
//        List<Integer> listNumbers = new ArrayList<>();
//        for (int i = 1; i <= usersInTravel.size(); i++) {
//            listNumbers.add(i);
//        }
//        model.addAttribute("listNumbers", listNumbers);
        model.addAttribute("route", new Route());
        List<LandingSite> landingSites = landingSiteService.listAllLandingSites();
        LocalTime time = LocalTime.parse(travel.getTimeParsed());
        for (LandingSite landingSite : landingSites) {
            LocalTime time1 = time.minusMinutes(Long.parseLong(landingSite.getTime()));
            landingSite.setTime(time1.toString());
        }
        landingSites.sort(Comparator.comparing(LandingSite::getTime));
        model.addAttribute("landingSites", landingSites);
        return "order-details";
    }

    @PostMapping ("/{date}/{id}")
    public String detailsOrderPost(@PathVariable("id") Long id, @PathVariable("date") String date, Route route, Principal principal, RedirectAttributes ra) {
        route.setStatus(true);
        timeNow = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        route.setRegistered(formatter.format(timeNow));
        route.setUser(userJPAService.findByUsername(principal.getName()));
        Travels travel = travelService.findTravelsById(id);
        route.setTravel(travel);
        LocalTime time = LocalTime.parse(travel.getTimeParsed());
        LocalTime time1 = time.minusMinutes(Long.parseLong(route.getLandingSite().getTime()));
        route.setLandingTime(time1.toString());
        Route saved = routeService.saveRoute(route);
//        ra.addFlashAttribute("message", "Route " + saved + " saved successfully");
        ra.addFlashAttribute("message", "Вы успешно записались на рейс");
        return "redirect:/order/{date}";
    }
}
