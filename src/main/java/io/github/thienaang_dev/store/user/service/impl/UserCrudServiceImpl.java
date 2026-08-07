package io.github.thienaang_dev.store.user.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.thienaang_dev.store.common.service.CrudService;
import io.github.thienaang_dev.store.user.entity.User;
import io.github.thienaang_dev.store.user.exception.UserNotFoundException;
import io.github.thienaang_dev.store.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCrudServiceImpl implements CrudService<User, UUID> {
  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = false)
  public User create(User entity) {
    return userRepository.save(entity);
  }

  @Override
  public User getById(UUID uuid) {
    return userRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public List<User> getAll() {
    return userRepository.findAll();
  }

  @Override
  @Transactional(readOnly = false)
  public User update(UUID uuid, User entity) {
    entity.setId(uuid);
    return userRepository.save(entity);
  }

  @Override
  @Transactional(readOnly = false)
  public void delete(UUID uuid) {
    userRepository.deleteById(uuid);
  }
}
