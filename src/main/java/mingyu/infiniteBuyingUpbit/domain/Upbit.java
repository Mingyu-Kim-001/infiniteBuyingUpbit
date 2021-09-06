package mingyu.infiniteBuyingUpbit.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    public static Map<String, String> getTickers(String authenticationToken){
        String fiat = "KRW"; //현재는 KRW시장만 지원
        Map<String, String> coins = new HashMap<>();
        try{
            URL url = new URL(serverUrl + "/v1/market/all");
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();
            System.out.println(result);
            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject coin = jsonArray.getJSONObject(i);
                if (coin.get("market").toString().split("-")[0].equals(fiat)) {
                    coins.put(coin.get("korean_name").toString(), coin.get("market").toString());
                }
            }
            return coins;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
