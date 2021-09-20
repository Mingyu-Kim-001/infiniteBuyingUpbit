package infiniteBuying.infiniteBuyingUpbit.controller;

import infiniteBuying.infiniteBuyingUpbit.domain.Asset;
import infiniteBuying.infiniteBuyingUpbit.domain.UpbitUtils;
import infiniteBuying.infiniteBuyingUpbit.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import infiniteBuying.infiniteBuyingUpbit.domain.Member;

import java.util.ArrayList;

@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    @GetMapping("/start")
    public String upbitKeyForm() {
        return "start";
    }

    @PostMapping(value = "/start")
    public String create(Member member) {
        String authenticationToken;
        ArrayList<Asset> assets;
        if((authenticationToken = UpbitUtils.auth(member.getAccessKey(), member.getSecretKey())) != null){
            if((assets = UpbitUtils.getAssets(member)) != null) {
                member.assets = assets;
                memberService.join(member);
                return "currentAssets";
            }
            else {
                return "fail/getAssetsFail";
            }
        }
        else{
            return "fail/authFail";
        }
    }
}
