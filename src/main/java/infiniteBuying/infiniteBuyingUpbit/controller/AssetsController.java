package infiniteBuying.infiniteBuyingUpbit.controller;

import infiniteBuying.infiniteBuyingUpbit.domain.Member;
import infiniteBuying.infiniteBuyingUpbit.domain.UpbitUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;


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
        Map<String, String> currentPrices = new HashMap<String, String>();
        for( String coinName : member.getCoins().keySet() ) {
            currentPrices.put(coinName, Double.toString(UpbitUtils.getCurrentPrice(coinName)));
            System.out.println(coinName + " " + currentPrices.get(coinName));
        }
        model.addAttribute("currentPrices", currentPrices);
        model.addAttribute("member", member);
        return "currentAssets";
    }
}
