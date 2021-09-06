package mingyu.infiniteBuyingUpbit.controller;

import mingyu.infiniteBuyingUpbit.domain.Member;
import mingyu.infiniteBuyingUpbit.domain.Upbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class AddCoinController {
    @Autowired
    Member member;

    @GetMapping("/addCoin")
    public String addCoin(Model model) {
        if(member == null) {
            return "fail/authFail";
        }
        System.out.println(member.getName());
        Map<String, String> tickers = Upbit.getTickers(Upbit.auth(member.getAccessKey(), member.getSecretKey()));
        model.addAttribute(tickers);
        return "addCoin";
    }
}
