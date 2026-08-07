package io.github.thienaang_dev.store.auth.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.thienaang_dev.store.auth.dto.AuthLoginRequestDto;
import io.github.thienaang_dev.store.auth.dto.AuthLoginResponseDto;
import io.github.thienaang_dev.store.auth.dto.AuthRegisterRequestDto;
import io.github.thienaang_dev.store.auth.service.JwtService;
import io.github.thienaang_dev.store.user.entity.User;
import io.github.thienaang_dev.store.user.exception.UserAlreadyExistsException;
import io.github.thienaang_dev.store.user.exception.UserNotFoundException;
import io.github.thienaang_dev.store.user.mapper.UserMapper;
import io.github.thienaang_dev.store.user.service.impl.UserCrudServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTests {
  @Mock
  private UserCrudServiceImpl userCrudServiceImpl;

  @Mock
  private UserMapper userMapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtService jwtService;

  @InjectMocks
  private AuthServiceImpl authService;

  @Test
  void givenNewUser_whenRegister_shouldCreateUser() {
    AuthRegisterRequestDto authRegisterRequestDto = new AuthRegisterRequestDto("username", "email", "password");

    when(userCrudServiceImpl.findOptionalByUsername("username")).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> authService.register(authRegisterRequestDto));
  }

  @Test
  void givenExistingUser_whenRegister_shouldThrowException() {
    AuthRegisterRequestDto authRegisterRequestDto = new AuthRegisterRequestDto("username", "email", "password");
    User user = new User(UUID.randomUUID(), "username", "email", "password", List.of(), false);

    when(userCrudServiceImpl.findOptionalByUsername("username")).thenReturn(Optional.of(user));

    assertThrows(UserAlreadyExistsException.class, () -> authService.register(authRegisterRequestDto));
  }

  @Test
  void givenExistingUser_whenLogin_shouldResponse() {
    AuthLoginRequestDto authLoginRequestDto = new AuthLoginRequestDto("username", "password");
    User user = new User(UUID.randomUUID(), "username", "email", "password", List.of(), false);
    Date expiryDate = new Date(new Date().getTime() + 86400000);

    when(userCrudServiceImpl.findByUsername("username")).thenReturn(user);
    when(jwtService.generateToken("username")).thenReturn("token");
    when(jwtService.extractExpiryDate("token")).thenReturn(expiryDate);

    AuthLoginResponseDto authLoginResponseDto = authService.login(authLoginRequestDto);
    assertDoesNotThrow(() -> authService.login(authLoginRequestDto));
    assertEquals("username", authLoginResponseDto.getUsername());
    assertEquals("token", authLoginResponseDto.getToken());
    assertEquals(expiryDate.toString(), authLoginResponseDto.getExpiry().toString());
  }

  @Test
  void givenNonExistingUser_whenLogin_shouldThrowException() {
    AuthLoginRequestDto authLoginRequestDto = new AuthLoginRequestDto("username", "password");

    when(userCrudServiceImpl.findByUsername("username")).thenThrow(UserNotFoundException.class);

    assertThrows(UserNotFoundException.class, () -> authService.login(authLoginRequestDto));
  }
}
