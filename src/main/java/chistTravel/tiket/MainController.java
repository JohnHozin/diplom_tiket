package chistTravel.tiket;

import chistTravel.tiket.db.entity.RoleJPA;
import chistTravel.tiket.db.entity.UserJPA;
import chistTravel.tiket.db.repository.UserRepository;
import chistTravel.tiket.service.RoleJPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("")
public class MainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleJPAService roleJPAService;

    @GetMapping("")
    public String showHomePage(Model model) {
        String role = getRoles();
        model.addAttribute("role", role);
        LocalDateTime today = LocalDateTime.from(ZonedDateTime.now(ZoneId.of("Africa/Addis_Ababa")));
        LocalDateTime tomorrow = today.plusDays(1);
        LocalDateTime dayAfterTomorrow = today.plusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        model.addAttribute("today", formatter.format(today));
        model.addAttribute("tomorrow", formatter.format(tomorrow));
        model.addAttribute("dayAfterTomorrow", formatter.format(dayAfterTomorrow));
        return "index";
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
