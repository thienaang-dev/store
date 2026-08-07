package io.github.thienaang_dev.store.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginResponseDto {
  @JsonProperty("username")
  private String username;

  @JsonProperty("token")
  private String token;

  @JsonProperty("expiry")
  private String expiry;
}

