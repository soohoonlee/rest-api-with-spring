package me.ssoon.demoinflearnrestapi.configs;

import me.ssoon.demoinflearnrestapi.accounts.AccountService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

  private PasswordEncoder passwordEncoder;

  private AuthenticationManager authenticationManager;

  private AccountService accountService;

  private TokenStore tokenStore;

  public AuthServerConfig(final PasswordEncoder passwordEncoder,
      final AuthenticationManager authenticationManager,
      final AccountService accountService,
      final TokenStore tokenStore) {
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.accountService = accountService;
    this.tokenStore = tokenStore;
  }

  @Override
  public void configure(final AuthorizationServerSecurityConfigurer security) throws Exception {
    security.passwordEncoder(passwordEncoder);
  }

  @Override
  public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
        .withClient("myApp")
        .authorizedGrantTypes("password", "refresh_token")
        .scopes("read", "write")
        .secret(this.passwordEncoder.encode("pass"))
        .accessTokenValiditySeconds(10 * 60)
        .refreshTokenValiditySeconds(6 * 10 * 60);
  }

  @Override
  public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager)
        .userDetailsService(accountService)
        .tokenStore(tokenStore);
  }
}
