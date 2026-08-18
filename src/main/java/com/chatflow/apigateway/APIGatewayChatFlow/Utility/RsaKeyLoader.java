package com.chatflow.apigateway.APIGatewayChatFlow.Utility;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RsaKeyLoader {
    public static RSAPublicKey loadPublicKey(String path) throws Exception {
        String key = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        key = key
                .replace("-----BEGIN PUBLIC KEY-----","")
                .replace("-----END PUBLIC KEY-----","")
                .replaceAll("\\s","");
        byte[] decodedKey = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey result = (RSAPublicKey) kf.generatePublic(spec);
        System.out.println("Public key: "+result.toString());
        return result;
    }
}
