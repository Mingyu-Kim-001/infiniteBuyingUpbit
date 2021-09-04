package mingyu.infiniteBuyingUpbit.domain;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class Member {

    private Long id;
    private String name;
    private String accessKey;
    private String secretKey;
    private ArrayList assets = new ArrayList<Asset>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAccessKey() {
        return accessKey;
    }
    public String getSecretKey() {
        return secretKey;
    }
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public ArrayList<Asset> getAssets() {
        return assets;
    }

    public void setAssets(ArrayList<Asset> assets) {
        this.assets = assets;
    }
}