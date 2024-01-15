package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.Driver;
import chistTravel.tiket.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Controller
@RequestMapping("/drivers")
public class DriverController {
    @Autowired
    private DriverService driverService;

    @GetMapping("")
    private String showAllUsers(Model model) {
        List<Driver> driverList = driverService.listAllDrivers();
        model.addAttribute("driverList", driverList);
        return "drivers-list";
    }

    @GetMapping("/new")
    public String showNewDriverForm(Model model) {
        model.addAttribute("driver", new Driver());
        return "driver-form";
    }

    @PostMapping("/new")
    private String saveNewDriver(Driver driver, RedirectAttributes ra) {
        // сохраняем студента в БД
        Driver saved = driverService.saveDriver(driver);
        // сообщение о том что студент добавлен
        ra.addFlashAttribute("message", "Driver " + saved + " saved successfully");
        return "redirect:/drivers";
    }
}
