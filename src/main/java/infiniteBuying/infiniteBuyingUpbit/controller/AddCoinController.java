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

@Controller
public class AddCoinController {
//    private final MemberRepository memberRepository;
//    @Autowired
//    public AddCoinController(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }
//    @Autowired
//    Member member;

    private final Member member;

    @Autowired
    public AddCoinController(Member member) {
        this.member = member;
    }

    @GetMapping("/addCoin")
    public String dropDown(Model model){
        ArrayList<String> tickerNames = UpbitUtils.getTickers();
//        System.out.println(tickerNames);
        model.addAttribute("tickerNames", tickerNames);
        model.addAttribute("coin", new Coin());
        return "addCoin";
    }

    @PostMapping("/addCoin")
    public String addCoin(@ModelAttribute @Valid Coin coin){

//        Member member = memberRepository.findAll().get(0); //일단 임시처리
//        System.out.println(member.getName());
//        Map<String, String> tickers = Upbit.getTickers(Upbit.auth(member.getAccessKey(), member.getSecretKey()));

//        System.out.println(CoinName);
//        model.addAttribute("coin", coin);
//        coin.setCoinName("KRW-BTC");
        coin.setMinimumBuying(Math.round(coin.getTotalBudget() / 40 / 2));
        System.out.println(coin.getCoinName());
        System.out.println(coin.getMinimumBuying());
        member.coins.put(coin.getCoinName(), coin);
        infiniteBuyingLogic.batch(member);
        return "home";
    }
}
