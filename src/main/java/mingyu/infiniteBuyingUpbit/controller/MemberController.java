package mingyu.infiniteBuyingUpbit.controller;

import mingyu.infiniteBuyingUpbit.domain.Asset;
import mingyu.infiniteBuyingUpbit.domain.Upbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import mingyu.infiniteBuyingUpbit.domain.Member;
import org.springframework.ui.Model;

import java.util.ArrayList;

@Controller
public class MemberController {

    @GetMapping("/start")
    public String upbitKeyForm() {
        return "start";
    }

    @PostMapping(value = "/start")
    public String create(Member member) {
        String authenticationToken;
        ArrayList<Asset> assets;
        if((authenticationToken = Upbit.auth(member.getAccessKey(),member.getSecretKey())) != null){
            member.setAuthenticationToken(authenticationToken);
            if ((assets = Upbit.getAssets(member.getAuthenticationToken())) != null) {
                member.setAssets(assets);
            }
            else{
                return "fail/getAssetsFail";
            }
            return "currentAssets";
        }
        else{
            return "fail/authFail";
        }
    }
}
