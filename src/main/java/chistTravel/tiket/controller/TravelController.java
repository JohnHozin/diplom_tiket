package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.*;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/travels")
public class TravelController {

    @Autowired
    private TravelService travelService;
    @Autowired
    private BusService busService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private LandingSiteService landingSiteService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleJPAService roleJPAService;

    @GetMapping("")
    private String showAllTravels(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        List<Travels> travelsLists = travelService.listAllTravels();
        travelsLists.sort(Comparator.comparing(Travels::getDateFull));
        model.addAttribute("travelsLists", travelsLists);
        return "travels-list";
    }

    @GetMapping("/new")
    public String showNewTravelsForm(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        model.addAttribute("travels", new Travels());
        List<Bus> busList = busService.listAllBus();
        model.addAttribute("busList", busList);
        List<Driver> driverList = driverService.listAllDrivers();
        model.addAttribute("driverList", driverList);
        return "travel-form";
    }

    @PostMapping("/new")
    private String saveNewTravels(Travels travels, RedirectAttributes ra) {
        LocalDateTime time = LocalDateTime.parse(travels.getTimeToDrive());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        travels.setDateFull(time.toString());
        travels.setTimeToDrive(formatter.format(time));
        travels.setDateParsed(formatter.format(time).split(" ")[0]);
        travels.setTimeParsed(formatter.format(time).split(" ")[1]);
        Travels saved = travelService.saveTravel(travels);
        ra.addFlashAttribute("message", "Маршрут сохранён");
        return "redirect:/travels";
    }

    @GetMapping("/{date}/{id}")
    public String detailsOrder(@PathVariable("id") Long id, @PathVariable("date") String date, Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        String[] dateSplit = date.split(":");
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
        for (LandingSite landingSite : landingSites) {
            LocalTime time1 = time.minusMinutes(Long.parseLong(landingSite.getTime()));
            landingSite.setTime(time1.toString());
        }
        landingSites.sort(Comparator.comparing(LandingSite::getTime));
        model.addAttribute("landingSites", landingSites);
        return "travels-details";
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
