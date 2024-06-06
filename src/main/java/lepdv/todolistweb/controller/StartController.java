package lepdv.todolistweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class StartController {

    @GetMapping("/start-page")
    public String startPage() {
        return "start_page";
    }

}
