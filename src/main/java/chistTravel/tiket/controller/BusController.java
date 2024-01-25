package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.Bus;
import chistTravel.tiket.db.entity.RoleJPA;
import chistTravel.tiket.db.entity.UserJPA;
import chistTravel.tiket.db.repository.UserRepository;
import chistTravel.tiket.service.BusService;
import chistTravel.tiket.service.RoleJPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/bus")
public class BusController {
    @Autowired
    private BusService busService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleJPAService roleJPAService;

    @GetMapping("")
    private String showAllBus(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        List<Bus> busList = busService.listAllBus();
        model.addAttribute("busList", busList);
        return "bus-list";
    }

    @GetMapping("/new")
    public String showNewBus(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        model.addAttribute("bus", new Bus());
        model.addAttribute("busNew", "yes");
        return "bus-form";
    }

    @PostMapping("/new")
    private String saveNewBus(Bus bus, RedirectAttributes ra) {
        busService.saveBus(bus);
        ra.addFlashAttribute("message", "Автобус успешно сохранён");
        return "redirect:/bus";
    }

    @GetMapping("/delete/{id}")
    public String deleteBus(@PathVariable("id") Long id, RedirectAttributes ra) {
        busService.deleteBusById(id);
        ra.addFlashAttribute("message", "Водитель удалён");
        return "redirect:/bus";
    }

    @GetMapping("/update/{id}")
    public String updateBus(@PathVariable("id") Long id, Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        Bus bus = busService.findBusById(id);
        model.addAttribute("bus", bus);
        model.addAttribute("busNew", "no");
        return "bus-form";
    }

    @PostMapping("/update/{id}")
    private String updateBus(@PathVariable("id") Long id, Bus bus, RedirectAttributes ra) {
        busService.updateBus(id, bus);
        ra.addFlashAttribute("message", "Автобус успешно изменён");
        return "redirect:/bus";
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
