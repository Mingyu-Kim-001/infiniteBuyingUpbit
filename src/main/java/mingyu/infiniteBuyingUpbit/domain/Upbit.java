package mingyu.infiniteBuyingUpbit.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
        return authenticationToken;
    }

    public static ArrayList<Asset> getAssets(String authenticationToken) {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200){
                System.out.println("getAssets" + statusCode);
                return null;
            }
            String entityString = EntityUtils.toString(entity, "UTF-8");
            JSONArray jsonArray = new JSONArray(entityString);
            ArrayList<Asset> assets = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject currentAsset = jsonArray.getJSONObject(i);
                Asset asset = new Asset();
                asset.setCurrency(currentAsset.get("currency").toString());
                asset.setBalance(currentAsset.getDouble("balance"));
                asset.setLocked(currentAsset.getDouble("locked"));
                asset.setAvg_buy_price(currentAsset.getDouble("avg_buy_price"));
                assets.add(asset);
            }
            return assets;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
