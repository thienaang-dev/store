package io.github.thienaang_dev.store.auth.service.impl;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.github.thienaang_dev.store.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
  @Value("${spring.jwt.secret}")
  private String secret;

  @Value("${spring.jwt.expiration}")
  private long expiration; // in milisecond

  private SecretKey getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  @SuppressWarnings("deprecation")
  private Claims extractAllClaim(String token) {
    return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
  }

  @Override
  public String extractUsername(String token) {
    Claims claims = extractAllClaim(token);
    return claims.getSubject();
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    return extractUsername(token).equals(userDetails.getUsername()) && extractExpiryDate(token).after(new Date());
  }

  @SuppressWarnings("deprecation")
  @Override
  public String generateToken(String username) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
    .setSubject(username)
    .setIssuedAt(now)
    .setExpiration(expiryDate)
    .signWith(getSignKey())
    .compact();
  }

  @Override
  public Date extractExpiryDate(String token) {
    Claims claims = extractAllClaim(token);
    return claims.getExpiration();
  }

}
