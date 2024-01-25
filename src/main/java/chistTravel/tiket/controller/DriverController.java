package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.*;
import chistTravel.tiket.db.repository.DriverRepo;
import chistTravel.tiket.db.repository.UserRepository;
import chistTravel.tiket.service.DriverService;
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
@RequestMapping("/drivers")
public class DriverController {
    @Autowired
    private DriverService driverService;
    @Autowired
    private UserJPAService userJPAService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DriverRepo driverRepo;
    @Autowired
    private RoleJPAService roleJPAService;

    @GetMapping("")
    private String showAllDrivers(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        List<Driver> driverList = driverService.listAllDrivers();
        for(Driver driver: driverList) {
            List<Travels> travelsLists = driver.getDriverTravels();
            travelsLists.sort(Comparator.comparing(Travels::getDateFull));
        }
        model.addAttribute("driverList", driverList);
        return "drivers-list";
    }

    @GetMapping("/new")
    public String showNewDriverForm(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        model.addAttribute("driver", new Driver());
        return "driver-form";
    }

    @PostMapping("/new")
    private String saveNewDriver(@ModelAttribute("driver") Driver driver, Model model, RedirectAttributes ra) {
        if (userJPAService.findByUsername(driver.getUsername()) != null) {
            if (userJPAService.findByUsername(driver.getUsername()).getUsername().equals(driver.getUsername())) {
                model.addAttribute("usernameError", "Пользователь с таким телефоном уже существует");
                return "driver-form";
            }
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        String hashedPassword = passwordEncoder.encode(driver.getPassword());
        driver.setPassword(hashedPassword);
        ArrayList<RoleJPA> roles = new ArrayList<>();
        roles.add(roleJPAService.listAllRoles().get(1)); // user
        roles.add(roleJPAService.listAllRoles().get(3)); // driver
        driver.setRolesJPA(roles);

        LocalDateTime time = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        driver.setRegistered(formatter.format(time));
        driver.setWrittenByDispatcher(false);
        driverService.saveDriver(driver);
        ra.addFlashAttribute("message", "Водитель успешно зарегистрирован");
        return "redirect:/drivers";
    }

    @GetMapping("/delete/{id}")
    public String deleteDriver(@PathVariable("id") Long id, RedirectAttributes ra) {
        driverService.deleteDriverById(id);
        ra.addFlashAttribute("message", "Водитель удалён");
        return "redirect:/drivers";
    }

    @GetMapping("/accountDriver")
    public String personalAccountDriver(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        String authenticationName = SecurityContextHolder.getContext().getAuthentication().getName();
        Driver driver = driverRepo.findByUsername(authenticationName);
        List<Travels> travels = driver.getDriverTravels();
        travels.sort(Comparator.comparing(Travels::getDateFull));
        model.addAttribute("driver", driver);
        model.addAttribute("travels", travels);
        return "driver-details";
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
