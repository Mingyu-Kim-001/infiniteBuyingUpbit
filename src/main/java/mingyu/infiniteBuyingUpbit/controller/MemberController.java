package mingyu.infiniteBuyingUpbit.controller;

import mingyu.infiniteBuyingUpbit.domain.Upbit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import mingyu.infiniteBuyingUpbit.domain.Member;

@Controller
public class MemberController {
    @GetMapping("/start")
    public String upbitKeyForm() {
        return "getUpbitKey";
    }

    @PostMapping(value = "/start")
    public String create(Member member) {
        String authenticationToken;
        if((authenticationToken = Upbit.auth(member.getAccessKey(),member.getSecretKey())) != null){
            member.setAuthenticationToken(authenticationToken);
            return "redirect:/";
        }
        else{
            return "fail/authFail";
        }
    }
}
