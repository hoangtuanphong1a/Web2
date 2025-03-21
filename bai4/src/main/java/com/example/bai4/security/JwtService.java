package com.example.bai4.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    // Khóa bí mật cho JWT (nên lưu trong file cấu hình hoặc biến môi trường)
    public static final String SECRET = "weroiuwroiweurieworuewioruewioruioewrewlkdsjflkjdsfkldsjfklsdf";

    // Tạo token với username
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    // Tạo token với claims và subject (username)
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token hết hạn sau 30 phút
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Lấy khóa ký
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Trích xuất username từ token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Trích xuất ngày hết hạn từ token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Trích xuất một claim từ token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Trích xuất tất cả claims từ token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Kiểm tra token có hết hạn không
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Xác thực token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
