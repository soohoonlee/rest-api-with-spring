package me.ssoon.demoinflearnrestapi.accounts;

import static me.ssoon.demoinflearnrestapi.accounts.AccountRole.ADMIN;
import static me.ssoon.demoinflearnrestapi.accounts.AccountRole.USER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

  @Autowired
  private AccountService accountService;

  @Autowired
  private AccountRepository accountRepository;

  @Test
  public void findByUsername() {
    // Given
    final String username = "jjinimania@gmail.com";
    final String password = "soohoon";
    final Account account = Account.builder()
        .email(username)
        .password(password)
        .roles(Set.of(ADMIN, USER))
        .build();
    this.accountRepository.save(account);

    // When
    final UserDetailsService userDetailsService = this.accountService;
    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    // Then
    assertThat(userDetails.getPassword()).isEqualTo(password);
  }
}