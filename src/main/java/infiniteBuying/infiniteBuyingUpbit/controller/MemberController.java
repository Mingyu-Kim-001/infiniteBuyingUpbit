package infiniteBuying.infiniteBuyingUpbit.controller;

import infiniteBuying.infiniteBuyingUpbit.domain.Asset;
import infiniteBuying.infiniteBuyingUpbit.domain.MemberForm;
import infiniteBuying.infiniteBuyingUpbit.domain.UpbitUtils;
import infiniteBuying.infiniteBuyingUpbit.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import infiniteBuying.infiniteBuyingUpbit.domain.Member;

import java.util.ArrayList;

@Controller
public class MemberController {
//    private final MemberService memberService;

//    @Autowired
//    public MemberController(MemberService memberService) {
//        this.memberService = memberService;
//    }

    private final Member member;
    @Autowired
    public MemberController(Member member) {
        this.member = member;
    }

    @GetMapping("/start")
    public String upbitKeyForm() {
        return "start";
    }

    @PostMapping(value = "/start")
    public String create(Model model, MemberForm memberForm) {
        String authenticationToken;
        ArrayList<Asset> assets;
//        System.out.println(memberForm.getName() + memberForm.getAccessKey() + memberForm.getSecretKey());
        member.setName(memberForm.getName());
        member.setAccessKey(memberForm.getAccessKey());
        member.setSecretKey(memberForm.getSecretKey());
//        System.out.println(member.getName() + member.getAccessKey());
        model.addAttribute("member", member);
        if((authenticationToken = UpbitUtils.auth(member.getAccessKey(), member.getSecretKey())) != null){
            if((assets = UpbitUtils.getAssets(member)) != null) {
                for (Asset asset : assets) {
                    member.getAssets().add(asset);
                }
                System.out.println(member.getAssets().get(0).getBalance());
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
