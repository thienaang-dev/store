package io.github.thienaang_dev.store.user.exception;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String username) {
    super("User " + username + " already exists");
  }
}
