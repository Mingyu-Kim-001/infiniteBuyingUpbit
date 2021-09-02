package mingyu.infiniteBuyingUpbit.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class Upbit {
    private static String serverUrl = "https://api.upbit.com";
    public static String auth(String accessKey, String secretKey) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;
        if(getAssets(authenticationToken) == null){
            return null;
        }
        return authenticationToken;
    }

    public static String getAssets(String authenticationToken) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            System.out.println(entity.getClass().getName());
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200){
                return null;
            }

            String entityString = EntityUtils.toString(entity, "UTF-8");
            System.out.println(entityString);
            System.out.println(entity.getContent());
            return entityString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
