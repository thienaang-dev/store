package io.github.thienaang_dev.store.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.thienaang_dev.store.auth.dto.AuthRegisterRequestDto;
import io.github.thienaang_dev.store.auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth", description = "Authentication")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @Tag(name = "Register", description = "Register a new user")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration request", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthRegisterRequestDto.class)))
  @PostMapping("/register")
  public ResponseEntity<Void> register(
      @Valid @RequestBody AuthRegisterRequestDto authRegisterRequestDto) {
    authService.register(authRegisterRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
