package io.github.thienaang_dev.store.auth.service;

import io.github.thienaang_dev.store.auth.dto.AuthRegisterRequestDto;
import io.github.thienaang_dev.store.user.exception.UserAlreadyExistsException;

public interface AuthService {
  /**
   * Register a new user or throw {@link UserAlreadyExistsException}
   *
   * @param authRegisterRequestDto User registration request
   */
  void register(AuthRegisterRequestDto authRegisterRequestDto);
}
