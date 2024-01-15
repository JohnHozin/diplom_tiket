package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.*;
import chistTravel.tiket.service.BusService;
import chistTravel.tiket.service.DriverService;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/travels")
public class TravelController {

    @Autowired
    private TravelService travelService;

    @Autowired
    private BusService busService;

    @Autowired
    private DriverService driverService;

    private LocalDateTime timeNow;

    @GetMapping("")
    private String showAllTravels(Model model) {
        List<Travels> travelsLists = travelService.listAllTravels();
        travelsLists.sort(Comparator.comparing(Travels::getDateFull));
        model.addAttribute("travelsLists", travelsLists);
        return "travels-list";
    }


}
