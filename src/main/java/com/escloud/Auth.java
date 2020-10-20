package com.escloud;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;
import java.util.Random;

public class Auth {
    protected String accessKey;

    protected String secretKey;

    public Auth(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    private static byte[] hamcsha1(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String makeSignature(String text, String key) {
        return new String(Base64.getEncoder().encode(hamcsha1(text.getBytes(), key.getBytes())), java.nio.charset.Charset.forName("UTF-8")).replace('+', '-').replace('/', '_');
    }


    /**
     * @param uri
     * @param lifetime
     * @return
     */
    public String makeRequestAuthorization(String uri, int lifetime) {
        JWTCreator.Builder builder = JWT.create();

        builder.withKeyId(this.accessKey);
        builder.withJWTId(getRandomString(16));
        builder.withExpiresAt(new Date(System.currentTimeMillis() + lifetime * 1000));

        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
        return "Bearer " + builder.sign(algorithm);
    }

    /**
     * @param uri
     * @param params
     * @param lifetime
     * @param nonce
     * @return
     * @throws UnsupportedEncodingException
     */
    public String makeRequestAuthorization(String uri, Map<String, String> params, int lifetime, boolean nonce) throws UnsupportedEncodingException {

        JSONObject jsonObj = new JSONObject(params);

        String nonceString;
        if (nonce) {
            nonceString = getRandomString(16);
        } else {
            nonceString = "no";
        }

        Long deadline = (System.currentTimeMillis() + lifetime * 1000) / 1000;

        String signature = this.makeSignature(nonceString + "\n" + deadline + "\n" + uri + "\n" + jsonObj.toString(), this.secretKey);

        System.out.print(signature);

        return "Signature " + this.accessKey + ":" + deadline + ":" + nonceString + ":" + signature;
    }

    /**
     * @param payload
     * @return
     */
    public String makeJwtToken(Map<String, Object> payload) {
        JWTCreator.Builder builder = JWT.create();

        for (String key : payload.keySet()) {
            if (payload.get(key) instanceof Boolean) {
                builder.withClaim(key, (Boolean) payload.get(key));
            } else if (payload.get(key) instanceof String) {
                builder.withClaim(key, (String) payload.get(key));
            } else if (payload.get(key) instanceof Date) {
                builder.withClaim(key, (Date) payload.get(key));
            }
        }
        builder.withJWTId(getRandomString(16));
        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
        return builder.sign(algorithm);
    }


    protected static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}

