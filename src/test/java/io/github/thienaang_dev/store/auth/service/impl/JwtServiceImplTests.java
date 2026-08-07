package io.github.thienaang_dev.store.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTests {
  private static final String SECRET = "3639b876a0f9109b8cd2f610c4d45efa8226c917f62baf175b872338c50f6b2817c7499efa3d7e9ed83557e4995727a5e432500ed8521b783e81a65c9f57152c";
  private static final long EXPIRATION = 30L * 24 * 60 * 60 * 1000;

  @InjectMocks
  private JwtServiceImpl jwtServiceImpl;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(jwtServiceImpl, "secret", SECRET);
    ReflectionTestUtils.setField(jwtServiceImpl, "expiration", EXPIRATION);
  }

  @Test
  void givenUsername_whenGenerateToken_shouldGenerateToken() {
    String username = "username";
    String token = jwtServiceImpl.generateToken(username);

    assertNotNull(token);
  }

  @Test
  void givenToken_whenExtractUsername_shouldExtractUsername() {
    String username = "username";
    String token = jwtServiceImpl.generateToken(username);
    String usernameInToken = jwtServiceImpl.extractUsername(token);

    assertEquals(username, usernameInToken);
  }

  @Test
  void givenValidToken_whenIsValidToken_shouldReturnTrue() {
    String token = jwtServiceImpl.generateToken("username");
    User user = new User("username", "password", List.of());

    assertTrue(jwtServiceImpl.isTokenValid(token, user));
  }

  @Test
  void givenInvalidUser_whenIsValidToken_shouldReturnFalse() {
    String token = jwtServiceImpl.generateToken("username");
    User user = new User("user", "password", List.of());

    assertFalse(jwtServiceImpl.isTokenValid(token, user));
  }

  @Test
  void givenToken_whenExtractExpiryDate_shouldReturnExpiryDate() {
    long beforeGeneration = System.currentTimeMillis();
    String token = jwtServiceImpl.generateToken("username");

    Date expiryDate = jwtServiceImpl.extractExpiryDate(token);

    long expectedExpiry = beforeGeneration + EXPIRATION;
    assertTrue(Math.abs(expiryDate.getTime() - expectedExpiry) < 1000);
  }
}
