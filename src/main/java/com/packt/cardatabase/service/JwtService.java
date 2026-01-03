package com.packt.cardatabase.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    // 토큰의 만료 시간: 1일 (밀리초). 실제 운영 시에는 짧아야 함
    static final long EXPIRATIONTIME = 86400000;
    // 토큰의 접두사 정의: 일반적으로 Bearer 스키미가 이용됨
    static final String PREFIX = "Bearer";

    // 비밀키 생성, 시연 목적으로만 이용
    // 운영 환경에서는 애플리케이션 구성에서 읽어들어와야 함
    static final Key key = Keys.secretKeyFor (SignatureAlgorithm.HS256);

    // 서명된 JWT 트콘 생성
    public String getToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(key)
                .compact();
        return token;
    }

    // 요청의 Authorization header에서 토큰을 가져온 뒤
    // 토큰을 확인하고 사용자 이름을 가져옴
    public String getAuthUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            String user = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace(PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                return user;
            }


        }
        return null;
    }
}
