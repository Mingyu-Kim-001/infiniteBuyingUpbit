package mingyu.infiniteBuyingUpbit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KeyController {
    @GetMapping("/start")
    public String upbitKeyForm() {
        return "getUpbitKey";
    }
}
