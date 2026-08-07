package io.github.thienaang_dev.store.auth.service;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
  String extractUsername(String token);

  boolean isTokenValid(String token, UserDetails userDetails);

  String generateToken(String username);

  Date extractExpiryDate(String token);
}
