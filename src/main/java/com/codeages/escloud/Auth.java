package com.codeages.escloud;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
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


    public String makeSignature(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(encrypt(text).getBytes("UTF-8")).replace('+', '-').replace('/', '_');
    }


    public static String encrypt(String srcStr) {
        try {
            StringBuilder result = new StringBuilder();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] bytes = md.digest(srcStr.getBytes("utf-8"));
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1)
                    result.append("0");
                result.append(hex);
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String makeRequestAuthorization(String uri, int lifetime) {
        JWTCreator.Builder builder = JWT.create();

        builder.withKeyId(this.accessKey);
        builder.withJWTId(getRandomString(16));
        builder.withExpiresAt(new Date(System.currentTimeMillis() + lifetime * 1000));

        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
        return "Bearer " + builder.sign(algorithm);
    }

    public String makeJwtToken(Map<String, Object> payload) {
        JWTCreator.Builder builder = JWT.create();

        for (String key : payload.keySet()) {
            if (payload.get(key).getClass().toString() == "Boolean") {
                builder.withClaim(key, (Boolean) payload.get(key));
            } else if (payload.get(key).getClass().toString() == "String") {
                builder.withClaim(key, (String) payload.get(key));
            } else if (payload.get(key).getClass().toString() == "Date") {
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

