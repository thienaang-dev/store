package io.github.thienaang_dev.store.auth.service;

import io.github.thienaang_dev.store.auth.dto.AuthLoginRequestDto;
import io.github.thienaang_dev.store.auth.dto.AuthLoginResponseDto;
import io.github.thienaang_dev.store.auth.dto.AuthRegisterRequestDto;
import io.github.thienaang_dev.store.user.exception.UserAlreadyExistsException;
import io.github.thienaang_dev.store.user.exception.UserNotFoundException;

public interface AuthService {
  /**
   * Register a new user or throw {@link UserAlreadyExistsException}
   *
   * @param authRegisterRequestDto User registration request
   */
  void register(AuthRegisterRequestDto authRegisterRequestDto);

  /**
   * Logs a user into the system or throw {@link UserNotFoundException}
   *
   * @param authLoginRequestDto User login request
   */
  AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto);
}
