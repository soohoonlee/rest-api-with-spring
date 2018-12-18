package me.ssoon.demoinflearnrestapi.configs;

import static me.ssoon.demoinflearnrestapi.accounts.AccountRole.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import me.ssoon.demoinflearnrestapi.accounts.Account;
import me.ssoon.demoinflearnrestapi.accounts.AccountRole;
import me.ssoon.demoinflearnrestapi.accounts.AccountService;
import me.ssoon.demoinflearnrestapi.common.AppProperties;
import me.ssoon.demoinflearnrestapi.common.BaseControllerTest;
import me.ssoon.demoinflearnrestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthServerConfigTest extends BaseControllerTest {

  @Autowired
  private AppProperties appProperties;

  @Test
  @TestDescription("인증 토큰을 발급 받는 테스트")
  public void getAuthToken() throws Exception {
    this.mockMvc.perform(post("/oauth/token")
        .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
        .param("username", appProperties.getUserUsername())
        .param("password", appProperties.getUserPassword())
        .param("grant_type", "password")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("access_token").exists());
  }
}