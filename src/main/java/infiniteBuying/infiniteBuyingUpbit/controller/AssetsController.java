package infiniteBuying.infiniteBuyingUpbit.controller;

import infiniteBuying.infiniteBuyingUpbit.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AssetsController {
    private final Member member;
    @Autowired
    public AssetsController(Member member) {
        this.member = member;
    }

    @GetMapping("/currentAssets")
    public String showAssetsTable(Model model){
        if(member == null){
            return "fail/authFail";
        }
        model.addAttribute("member", member);
        return "currentAssets";
    }
}
