package mingyu.infiniteBuyingUpbit.controller;

import mingyu.infiniteBuyingUpbit.domain.Coin;
import mingyu.infiniteBuyingUpbit.domain.Member;
import mingyu.infiniteBuyingUpbit.domain.UpbitUtils;
import mingyu.infiniteBuyingUpbit.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    @Autowired
    public AddCoinController(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    @Autowired
    Member member;

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
        coin.setCoinName("KRW-BTC");
        System.out.println(coin.getCoinName());
        System.out.println(coin.getBuyingAmount());
        member.coins.put(coin.getCoinName(), coin);
        return "home";
    }
}
