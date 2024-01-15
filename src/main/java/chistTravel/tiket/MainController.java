package chistTravel.tiket;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("")
public class MainController {

    @GetMapping("")
    public String showHomePage(Model model){
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime dayAfterTomorrow = LocalDateTime.now().plusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        model.addAttribute("today",formatter.format(today));
        model.addAttribute("tomorrow",formatter.format(tomorrow));
        model.addAttribute("dayAfterTomorrow",formatter.format(dayAfterTomorrow));
        return "index"; // index.html - шаблон
    }
}
