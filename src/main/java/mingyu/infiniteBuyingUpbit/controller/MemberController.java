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
    public String create(MemberForm memberForm) {
        String authenticationToken;
        if((authenticationToken = Upbit.auth(memberForm.getAccessKey(),memberForm.getSecretKey())) != null){
            Member member = new Member();
            member.setAccessKey(memberForm.getAccessKey());
            member.setSecretKey(memberForm.getSecretKey());
            member.setName(memberForm.getName());
            member.setAuthenticationToken(authenticationToken);
            return "redirect:/";
        }
        else{
            return "fail/authFail";
        }
    }
}
