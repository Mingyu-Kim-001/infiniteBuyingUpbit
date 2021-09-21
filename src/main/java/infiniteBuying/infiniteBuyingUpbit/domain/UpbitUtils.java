package infiniteBuying.infiniteBuyingUpbit.domain;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.google.gson.Gson;

//업비트와 통신을 통해 데이터 주고받는 함수들 & 기타 유틸 함수들
public class UpbitUtils {
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
    public static double getPriceUnit(double price){
        /*
        업비트의 경우 호가단위가 아래와 같다(21년 4월 21일 기준).
        2,000,000원 이상 : 1,000원
        1,000,000원 이상~2,000,000원 미만 : 500원
        100,000원 이상~1,000,000원 미만 : 50원
        10,000원 이상~100,000원 미만 : 10원
        1,000원 이상~10,000원 : 5원
        1,000원 미만 : 1원
        100원 : 0.1원
        10원 미만 : 0.01원
         */
        if(price >= 2000000) return 1000;
        if(price >= 1000000) return 500;
        if(price >= 100000) return 50;
        if(price >= 10000) return 10;
        if(price >= 1000) return 5;
        if(price >= 100) return 1;
        if(price >= 10) return 0.1;
        return 0.01;
    }

    //호가 단위에 가격을 맞춘다.
    public static int setPriceToUnit(double price){
        /*
        ex) 호가단위가 5원일시, 매수가격 2552원에 매수주문을 넣지 못한다. 2550원이나 2555로 단위를 맞춰서 주문을 넣어야 됨. 이를 맞춰 주는 함수
        price : 단위를 맞추려는 가격
        */
        double unit = UpbitUtils.getPriceUnit(price);
        return (int)Math.round(Math.round(price / unit) * unit);
    }

    public static ArrayList<String> getTickers(){
        String fiat = "KRW"; //현재는 KRW시장만 지원
        ArrayList<String> coins = new ArrayList<String>();
        try{
            URL url = new URL(serverUrl + "/v1/market/all");
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();
            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject coin = jsonArray.getJSONObject(i);
                if (coin.get("market").toString().split("-")[0].equals(fiat)) {
                    coins.add(coin.get("market").toString());
                }
            }
            return coins;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getOrder(Member member, Order order) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String accessKey = member.getAccessKey();
        String secretKey = member.getSecretKey();
        String uuid = order.getUuid();
        HashMap<String, String> params = new HashMap<>();
        params.put("uuid", uuid);

        ArrayList<String> queryElements = new ArrayList<>();
        for (Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl + "/v1/order?" + queryString);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getOrders(Member member, ArrayList<Order> orders) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String accessKey = member.getAccessKey();
        String secretKey = member.getSecretKey();
        ArrayList<String> uuids = new ArrayList<String>();
        for (Order order : orders) {
            uuids.add(order.getUuid());
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("state", "done");

        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }
        for(String uuid : uuids) {
            queryElements.add("uuids[]=" + uuid);
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl + "/v1/orders?" + queryString);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Asset> getAssets(Member member) {
        String authenticationToken = UpbitUtils.auth(member.getAccessKey(), member.getSecretKey());
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
            ArrayList<Asset> assets = new ArrayList<Asset>();
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

    public static String deleteOrder(Member member, Order order) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String accessKey = member.getAccessKey();
        String secretKey = member.getSecretKey();
        String uuid = order.getUuid();

        HashMap<String, String> params = new HashMap<>();
        params.put("uuid", uuid);

        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));

        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpDelete request = new HttpDelete(serverUrl + "/v1/order?" + queryString);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String postOrders(Member member, String coinName, boolean isBuy, String volume, String price, boolean isMarket){
        /*
        member : 필수
        coinName : 필수
        isBuy : 필수
        volume : isBuy가 false일 때 필수.
        price : isBuy가 true일때 필수. (지정가 매수일 때는 매수가격. 즉 price가 1000이면 1000원 가격에 구매. 시장가 매수일 때는 총 매수량. 즉 price가 1000이면 1000원어치를 시장가에 매수)
        isMarket : 필수조건
         */
        String accessKey = member.getAccessKey();
        String secretKey = member.getSecretKey();
        String side = isBuy ? "bid" : "ask";
        volume = isMarket ? volume : "";
        String ord_type;
        if (!isMarket) {
            ord_type = "limit";
        } else if (isBuy) {
            ord_type = "price";
        } else {
            ord_type = "market";
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("market", coinName);
        params.put("side", side);
        params.put("volume", volume);
        params.put("price", price);
        params.put("ord_type", ord_type);

        ArrayList<String> queryElements = new ArrayList<>();
        for(Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(queryString.getBytes("UTF-8"));

            String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String jwtToken = JWT.create()
                    .withClaim("access_key", accessKey)
                    .withClaim("nonce", UUID.randomUUID().toString())
                    .withClaim("query_hash", queryHash)
                    .withClaim("query_hash_alg", "SHA512")
                    .sign(algorithm);

            String authenticationToken = "Bearer " + jwtToken;

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(serverUrl + "/v1/orders");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);
            request.setEntity(new StringEntity(new Gson().toJson(params)));

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double getCurrentPrice(String coinName) {

        try{
            URL url = new URL(serverUrl + "/v1/ticker");
            BufferedReader bf;

            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();
            JSONArray jsonArray = new JSONArray(result);

            double currentPrice = Double.parseDouble(jsonArray.getJSONObject(0).get("trade_price").toString());

            return currentPrice;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
