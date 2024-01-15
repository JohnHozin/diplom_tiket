package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.*;
import chistTravel.tiket.service.BusService;
import chistTravel.tiket.service.DriverService;
import chistTravel.tiket.service.LandingSiteService;
import chistTravel.tiket.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/dispatcher/travel")
public class DispatcherTravelController {

    @Autowired
    private TravelService travelService;

    @Autowired
    private BusService busService;

    @Autowired
    private LandingSiteService landingSiteService;

    @Autowired
    private DriverService driverService;

    private LocalDateTime time;

    @GetMapping("")
    private String showAllTravels(Model model) {
        List<Travels> travelsLists = travelService.listAllTravels();
        model.addAttribute("travelsLists", travelsLists);
        return "dispatcher-travels-list";
    }

    @GetMapping("/new")
    public String showNewTravelsForm(Model model) {
        model.addAttribute("travels", new Travels());
        List<Bus> busList = busService.listAllBus();
        model.addAttribute("busList", busList);
        List<Driver> driverList = driverService.listAllDrivers();
        model.addAttribute("driverList", driverList);
        return "travel-form";
    }

    @PostMapping("/new")
    private String saveNewTravels(Travels travels, RedirectAttributes ra) {

        time = LocalDateTime.parse(travels.getTimeToDrive());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        travels.setDateFull(time.toString());
        travels.setTimeToDrive(formatter.format(time));
        travels.setDateParsed(formatter.format(time).split(" ")[0]);
        travels.setTimeParsed(formatter.format(time).split(" ")[1]);
        Travels saved = travelService.saveTravel(travels);
        // сообщение о том что студент добавлен
        ra.addFlashAttribute("message", "Travels " + saved + " saved successfully");
        return "redirect:/dispatcher/travels";
    }

//    @GetMapping("/details/{id}")
//    public String detailsTravels(@PathVariable("id") Long id, Model model) {
//        Travels travels = travelService.findTravelsById(id);
//        model.addAttribute("travels", travels);
//        List<Route> usersInTravel = travels.getRoutes();
//        model.addAttribute("usersInTravel", usersInTravel);
//
//        List<Integer> listNumbers = new ArrayList<>();
//        for (int i =1; i <=usersInTravel.size(); i ++){
//            listNumbers.add(i);
//        }
//        model.addAttribute("listNumbers", listNumbers);
//        return "dispatcher-travels-details";
//    }

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
        return "dispatcher-order-details";
    }

}
