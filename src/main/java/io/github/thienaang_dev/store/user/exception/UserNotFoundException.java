package io.github.thienaang_dev.store.user.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(UUID uuid) {
    super("User " + uuid + " not found");
  }
}
