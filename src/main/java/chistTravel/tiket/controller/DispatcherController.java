package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.Route;
import chistTravel.tiket.db.entity.Travels;
import chistTravel.tiket.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/dispatcher")
public class DispatcherController {
    @Autowired
    private TravelService travelService;

    @GetMapping("")
    private String showAllTravels(Model model) {
        List<Travels> travelsLists = travelService.listAllTravels();
        model.addAttribute("travelsLists", travelsLists);
        return "dispatcher-list";
    }

    @GetMapping("/new/{id}")
    public String detailsTravels(@PathVariable("id") Long id, Model model) {
        Travels travels = travelService.findTravelsById(id);
        model.addAttribute("travels", travels);
        List<Route> usersInTravel = travels.getRoutes();
        model.addAttribute("usersInTravel", usersInTravel);

        List<Integer> listNumbers = new ArrayList<>();
        for (int i =1; i <=usersInTravel.size(); i ++){
            listNumbers.add(i);
        }
        model.addAttribute("listNumbers", listNumbers);
        model.addAttribute("listNumbersSize", listNumbers.size());
        return "dispatcher-form";
    }

}
