package io.github.thienaang_dev.store.auth.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.thienaang_dev.store.auth.dto.AuthLoginRequestDto;
import io.github.thienaang_dev.store.auth.dto.AuthLoginResponseDto;
import io.github.thienaang_dev.store.auth.dto.AuthRegisterRequestDto;
import io.github.thienaang_dev.store.auth.service.AuthService;
import io.github.thienaang_dev.store.auth.service.JwtService;
import io.github.thienaang_dev.store.user.dto.UserDto;
import io.github.thienaang_dev.store.user.entity.User;
import io.github.thienaang_dev.store.user.exception.UserAlreadyExistsException;
import io.github.thienaang_dev.store.user.mapper.UserMapper;
import io.github.thienaang_dev.store.user.service.impl.UserCrudServiceImpl;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final UserCrudServiceImpl userCrudService;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Override
  public void register(AuthRegisterRequestDto authRegisterRequestDto) {
    Optional<User> userOptional = userCrudService.findOptionalByUsername(authRegisterRequestDto.getUsername());

    if (userOptional.isPresent()) {
      throw new UserAlreadyExistsException(authRegisterRequestDto.getUsername());
    }

    UserDto userDto = UserDto.builder()
        .username(authRegisterRequestDto.getUsername())
        .email(authRegisterRequestDto.getEmail())
        .password(passwordEncoder.encode(authRegisterRequestDto.getPassword()))
        .roles(List.of("USER"))
        .build();
    User userEntity = userMapper.toEntity(userDto);
    userCrudService.create(userEntity);
  }

  @Override
  public AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {
    User user = userCrudService.findByUsername(authLoginRequestDto.getUsername());
    String token = jwtService.generateToken(user.getUsername());
    Date expiryDate = jwtService.extractExpiryDate(token);
    return AuthLoginResponseDto.builder().username(user.getUsername()).token(token).expiry(expiryDate.toString())
        .build();
  }
}
