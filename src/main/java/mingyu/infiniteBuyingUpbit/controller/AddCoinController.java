package mingyu.infiniteBuyingUpbit.controller;

import mingyu.infiniteBuyingUpbit.domain.Coin;
import mingyu.infiniteBuyingUpbit.domain.Member;
import mingyu.infiniteBuyingUpbit.domain.Upbit;
import mingyu.infiniteBuyingUpbit.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        Map<String, String> tickers = new HashMap<>();
        tickers.put("비트코인", "KRW-BTC");
        tickers.put("이더리움", "KRW-ETH");
        ArrayList<String> koreanCoinNames = new ArrayList<>(tickers.keySet());
//        Coin coin = new Coin();
//        model.addAttribute(coin);
        model.addAttribute("koreanCoinNames",koreanCoinNames);
//        model.addAttribute(tickers);
        ArrayList<String> tickerNames = new ArrayList<>();
        tickerNames.add("KRW-BTC");
        tickerNames.add("KRW-ETH");
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
        return "home";
    }
}
