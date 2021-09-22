package infiniteBuying.infiniteBuyingUpbit.controller;

import infiniteBuying.infiniteBuyingUpbit.domain.Coin;
import infiniteBuying.infiniteBuyingUpbit.domain.Member;
import infiniteBuying.infiniteBuyingUpbit.domain.UpbitUtils;
import infiniteBuying.infiniteBuyingUpbit.domain.infiniteBuyingLogic;
import infiniteBuying.infiniteBuyingUpbit.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AddCoinController {

    private final Member member;

    @Autowired
    public AddCoinController(Member member) {
        this.member = member;
    }

    @GetMapping("/addCoin")
    public String dropDown(Model model){
        if (member == null) {
            return "fail/authFail";
        }
        ArrayList<String> tickerNames = UpbitUtils.getTickers();
//        System.out.println(tickerNames);
        model.addAttribute("tickerNames", tickerNames);
        model.addAttribute("coin", new Coin());
        return "addCoin";
    }

    @PostMapping("/addCoin")
    public String addCoin(Model model, @ModelAttribute @Valid Coin coin){
        coin.setMinimumBuying(Math.round(coin.getTotalBudget() / 40 / 2));
        coin.setRemainingBudget(coin.getTotalBudget());
        System.out.println("addCoinController");
        System.out.println(coin.getCoinName());
        System.out.println(coin.getMinimumBuying());
        System.out.println(coin.getRemainingBudget());
        member.getCoins().put(coin.getCoinName(), coin);
        infiniteBuyingLogic.batch(member);


        model.addAttribute("member", member);

        return "currentAssets";
    }
}
