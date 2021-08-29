package spring.infinteBuyingUpbit.service;

//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//
//import java.io.UnsupportedEncodingException;
//import java.math.BigInteger;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.UUID;

//public class OpenApiSample {
//
//    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        String accessKey = "발급받은 Access key";
//        String secretKey = "발급받은 Secret key";
//
//        String queryString = "query string 생성";
//
//        MessageDigest md = MessageDigest.getInstance("SHA-512");
//        md.update(queryString.getBytes("utf8"));
//
//        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));
//
//        Algorithm algorithm = Algorithm.HMAC256(secretKey);
//        String jwtToken = JWT.create()
//                .withClaim("access_key", accessKey)
//                .withClaim("nonce", UUID.randomUUID().toString())
//                .withClaim("query_hash", queryHash)
//                .withClaim("query_hash_alg", "SHA512")
//                .sign(algorithm);
//
//        String authenticationToken = "Bearer " + jwtToken;
//    }
//
//}
public class KeyService {

}
