package mingyu.infiniteBuyingUpbit.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.UUID;

public class Member {

    private Long id;
    private String name;
    private String accessKey;
    private String secretKey;
    private String authenticationToken;

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
    public String getAuthenticationToken() {
        return authenticationToken;
    }
    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public boolean auth() {
        String serverUrl = "https://api.upbit.com/v1/accounts";
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                        .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String entityString = EntityUtils.toString(entity, "UTF-8");
            System.out.println(entityString);

            if (entityString.equals("404 Not Found")){
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        this.setAuthenticationToken("Bearer " + jwtToken);
        return true;
    }
}