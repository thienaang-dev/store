package io.github.thienaang_dev.store.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import io.github.thienaang_dev.store.user.dto.UserDto;
import io.github.thienaang_dev.store.user.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
  @Mapping(target = "isDeleted", ignore = true)
  User toEntity(UserDto dto);

  UserDto toDto(User entity);
}
