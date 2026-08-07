package io.github.thienaang_dev.store.auth.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.thienaang_dev.store.user.entity.User;
import io.github.thienaang_dev.store.user.service.impl.UserCrudServiceImpl;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserCrudServiceImpl userCrudServiceImpl;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userCrudServiceImpl.findByUsername(username);
    Set<GrantedAuthority> authorities = new HashSet<>();
    for (String role : user.getRoles()) {
      authorities.add(() -> role);
    }
    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
  }}
