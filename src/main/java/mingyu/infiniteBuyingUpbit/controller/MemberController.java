package mingyu.infiniteBuyingUpbit.controller;

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
        Member member = new Member();
        member.setAccessKey(memberForm.getAccessKey());
        member.setSecretKey(memberForm.getSecretKey());
        System.out.println(member.getAccessKey());
        return "redirect:/";
    }
}
