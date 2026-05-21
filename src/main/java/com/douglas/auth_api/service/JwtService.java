package com.douglas.auth_api.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service

public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    //GERA O TOKEN PARA UM USUARIO

    public String generateToken (UserDetails userDatails) {
        return Jwts.builder()
                .subject(userDatails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigninKey())
                .compact();

    }
    //VERIFICANDO SE O TOKEN É VALIDO
    public boolean isTokenValid(String token, UserDetails userDatails) {
        final String username = extractUsername(token);
        return (username.equals(userDatails.getUsername()) && !isTokenExpired(token));
    }

    //EXTRAI O EMAIL DO TOKEN
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //VERIFICA SE O TOKEN É EXPIRADO
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    //EXTRAI QUALQUER INFO DO TOKEN
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    //CONVERTE A CHAVE PARA O FORMATO QUE O JJWT ENTENDE
    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
