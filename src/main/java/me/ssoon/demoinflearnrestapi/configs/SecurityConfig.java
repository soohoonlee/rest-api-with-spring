package me.ssoon.demoinflearnrestapi.configs;

import static org.springframework.http.HttpMethod.GET;

import me.ssoon.demoinflearnrestapi.accounts.AccountService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private AccountService accountService;

  private PasswordEncoder passwordEncoder;

  public SecurityConfig(final AccountService accountService,
      final PasswordEncoder passwordEncoder) {
    this.accountService = accountService;
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  public TokenStore tokenStore() {
    return new InMemoryTokenStore();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(accountService)
        .passwordEncoder(passwordEncoder);
  }

  @Override
  public void configure(final WebSecurity web) throws Exception {
    web.ignoring().mvcMatchers("/docs/index.html");
    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http
        .anonymous()
          .and()
        .formLogin()
          .and()
        .authorizeRequests()
          .mvcMatchers(GET, "/api/**").authenticated()
          .anyRequest().authenticated();
  }
}
