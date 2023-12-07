package com.ll.sb231130.global.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

// JWT 라이브러리 추가, JwtUtil 클래스 도입, Jwt 토큰은 데이터와 서명으로 구성되어 있다.
public class JwtUtil {

    // Map<String, String> 형태의 데이터를 받아서 JWT 토큰을 생성하는 메서드
    public static String encode(Map<String, String> data) {
        // JWT의 클레임(claim) 정보를 설정하기 위한 객체 생성
        Claims claims = Jwts
                .claims()
                .setSubject("sb-23-11-30 jwt") // 클레임의 주제(subject) 설정
                .add("type", "access_token") // 사용자 정의 클레임으로 타입 설정
                .add("data", data) // 사용자 정의 클레임으로 데이터 설정
                .build();

        // 현재 시간을 나타내는 Date 객체 생성
        Date now = new Date();
        // 토큰의 유효 기간을 현재 시간에서 5분 뒤로 설정
        Date validity = new Date(now.getTime() + 1000 * 60 * 5); // 1000ms : 1초

        // JWT 빌더를 사용하여 토큰 생성
        return Jwts.builder()
                .setClaims(claims) // 클레임 정보 설정
                .setIssuedAt(now) // 토큰 발급 시간 설정
                .setExpiration(validity) // 토큰의 만료 시간 설정
                // HS256 서명 알고리즘을 사용하여 토큰에 서명을 추가합니다. 이때, 서명에 사용되는 비밀 키를 지정합니다.
                .signWith(SignatureAlgorithm.HS256,
                        "abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890")
                .compact(); // 최종적으로 JWT 토큰을 생성하고 반환
    }
}
