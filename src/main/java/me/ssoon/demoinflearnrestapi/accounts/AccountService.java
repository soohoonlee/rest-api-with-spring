package me.ssoon.demoinflearnrestapi.accounts;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

  private AccountRepository accountRepository;

  public AccountService(final AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final Account account = accountRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(username));
    return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
  }

  private Collection<? extends GrantedAuthority> authorities(final Set<AccountRole> roles) {
    return roles.stream()
        .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
        .collect(Collectors.toSet());
  }
}
