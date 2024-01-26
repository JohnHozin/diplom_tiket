package chistTravel.tiket;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// собственный контроллер обработки ошибок
@Controller
@RequestMapping()
public class AppErrorController implements ErrorController {
    // метод обработки запрос с ошибкой
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model){
        model.addAttribute("role", "USER");
        // ...
        model.addAttribute("message", "Error: " +
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        return "layout/error";
    }
}
