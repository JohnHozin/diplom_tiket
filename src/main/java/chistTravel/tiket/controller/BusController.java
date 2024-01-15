package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.Bus;
import chistTravel.tiket.db.entity.Driver;
import chistTravel.tiket.service.BusService;
import chistTravel.tiket.service.DriverService;
import chistTravel.tiket.service.UserJPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/bus")
public class BusController {
    @Autowired
    private BusService busService;

    @GetMapping("")
    private String showAllBus(Model model) {
        List<Bus> busList = busService.listAllBus();
        model.addAttribute("busList", busList);
        return "bus-list";
    }

    @GetMapping("/new")
    public String showNewBus(Model model) {
        model.addAttribute("bus", new Bus());
        return "bus-form";
    }

    @PostMapping("/new")
    private String saveNewBus(Bus bus, RedirectAttributes ra) {
        Bus saved = busService.saveBus(bus);
        ra.addFlashAttribute("message", "Bus " + saved + " saved successfully");
        return "redirect:/bus";
    }
}
