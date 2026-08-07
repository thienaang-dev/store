package io.github.thienaang_dev.store.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class AuthRegisterRequestDto {
  @NotBlank private String username;

  private String email;

  @NotBlank
  @Size(min = 8)
  private String password;
}
