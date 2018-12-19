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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

  private AccountRepository accountRepository;

  private PasswordEncoder passwordEncoder;

  public AccountService(final AccountRepository accountRepository,
      final PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Account saveAccount(final Account account) {
    account.setPassword(this.passwordEncoder.encode(account.getPassword()));
    return this.accountRepository.save(account);
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final Account account = accountRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(username));
    return new AccountAdapter(account);
  }
}
