package io.github.thienaang_dev.store.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.thienaang_dev.store.common.dto.ErrorResponseDto;

@RestControllerAdvice
public class UserExceptionHandler {
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder().message(ex.getMessage()).build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder().message(ex.getMessage()).build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
  }
}
