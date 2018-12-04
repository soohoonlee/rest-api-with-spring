package me.ssoon.demoinflearnrestapi.events;

import static me.ssoon.demoinflearnrestapi.events.EventStatus.DRAFT;
import static me.ssoon.demoinflearnrestapi.events.EventStatus.PUBLISHED;
import static org.hamcrest.Matchers.not;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_UTF8_VALUE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class EventControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void createdEvent() throws Exception {
    final Event event = Event.builder()
        .id(100)
        .name("Spring")
        .description("REST API Development with Spring")
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 12, 4, 12, 17))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 12, 5, 12, 17))
        .beginEventDateTime(LocalDateTime.of(2018, 12, 6, 12, 17))
        .endEventDateTime(LocalDateTime.of(2018, 12, 7, 12, 17))
        .basePrice(100)
        .maxPrice(200)
        .limitOfEnrollment(100)
        .location("강남역 D2 스타트업 팩토리")
        .free(true)
        .offline(false)
        .eventStatus(PUBLISHED)
        .build();

    mockMvc
        .perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(HAL_JSON)
            .content(objectMapper.writeValueAsString(event))
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").exists())
        .andExpect(header().exists(LOCATION))
        .andExpect(header().string(CONTENT_TYPE, HAL_JSON_UTF8_VALUE))
        .andExpect(jsonPath("id").value(not(100)))
        .andExpect(jsonPath("free").value(not(true)))
        .andExpect(jsonPath("eventStatus").value(DRAFT.name()))
    ;
  }
}
