package chistTravel.tiket.controller;

import chistTravel.tiket.db.entity.UserJPA;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signin")
public class SignInController {
    @GetMapping("")
    public String showNewRegistrationForm(Model model) {
//        model.addAttribute("userJPA", new UserJPA());
        return "sign-in";
    }

    @GetMapping("/examples")
    public String showNewRegistrationExapmples(Model model) {
        return "examples";
    }
}
