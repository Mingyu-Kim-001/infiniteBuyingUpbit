package mingyu.infiniteBuyingUpbit.controller;

import mingyu.infiniteBuyingUpbit.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AssetsController {
    @Autowired
    Member member;

    @GetMapping("/currentAssets")
    public String showAssetsTable(Model model){
        if(member == null){
            return "fail/authFail";
        }
        model.addAttribute("member", member);
        System.out.println(member.getAssets().get(0));
        return "currentAssets";
    }
}
