package com.chatflow.apigateway.APIGatewayChatFlow.Service;


import com.chatflow.apigateway.APIGatewayChatFlow.Utility.RsaKeyLoader;
import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;


@Service
public class JwtService {

    public String extractUserName(String jwtToken) throws Exception {
        Claims claims = extractAllClaims(jwtToken);
        return claims.getSubject();
    }

    private Claims extractAllClaims(String jwtToken) throws Exception {
        RSAPublicKey key = RsaKeyLoader.loadPublicKey("jwt-keys/public.pem");
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }


    public boolean validateToken(String jwtToken) throws Exception {
        Claims claims = extractAllClaims(jwtToken);
        String username = claims.getSubject();
        if(claims.getExpiration().before(new Date())) {
            return false;
        }
        return true;
    }
}
