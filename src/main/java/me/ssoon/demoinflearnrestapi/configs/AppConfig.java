package me.ssoon.demoinflearnrestapi.configs;

import static me.ssoon.demoinflearnrestapi.accounts.AccountRole.ADMIN;
import static me.ssoon.demoinflearnrestapi.accounts.AccountRole.USER;

import java.util.Set;
import me.ssoon.demoinflearnrestapi.accounts.Account;
import me.ssoon.demoinflearnrestapi.accounts.AccountService;
import me.ssoon.demoinflearnrestapi.common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public ApplicationRunner applicationRunner() {
    return new ApplicationRunner() {

      @Autowired
      private AccountService accountService;

      @Autowired
      private AppProperties appProperties;

      @Override
      public void run(final ApplicationArguments args) throws Exception {
        final Account admin = Account.builder()
            .email(appProperties.getAdminUsername())
            .password(appProperties.getAdminPassword())
            .roles(Set.of(ADMIN, USER))
            .build();
        accountService.saveAccount(admin);

        final Account user = Account.builder()
            .email(appProperties.getUserUsername())
            .password(appProperties.getUserPassword())
            .roles(Set.of(USER))
            .build();
        accountService.saveAccount(user);
      }
    };
  }
}
