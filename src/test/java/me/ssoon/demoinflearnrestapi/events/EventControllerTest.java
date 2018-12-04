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
import me.ssoon.demoinflearnrestapi.common.TestDescription;
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
  @TestDescription("정상적으로 이벤트를 생성하는 테스트")
  public void createEvent() throws Exception {
    final EventDto event = EventDto.builder()
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

  @Test
  @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
  public void createEventBadRequest() throws Exception {
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
        .andExpect(status().isBadRequest())
    ;
  }

  @Test
  @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
  public void createEventBadRequestEmptyInput() throws Exception {
    final EventDto eventDto = EventDto.builder().build();

    this.mockMvc.perform(post("/api/events")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .accept(HAL_JSON)
        .content(objectMapper.writeValueAsString(eventDto))
    )
        .andExpect(status().isBadRequest());
  }

  @Test
  @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
  public void createEventBadRequestWrongInput() throws Exception {
    final EventDto eventDto = EventDto.builder()
        .name("Spring")
        .description("REST API Development with Spring")
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 12, 7, 12, 17))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 12, 6, 12, 17))
        .beginEventDateTime(LocalDateTime.of(2018, 12, 5, 12, 17))
        .endEventDateTime(LocalDateTime.of(2018, 12, 4, 12, 17))
        .basePrice(10000)
        .maxPrice(200)
        .limitOfEnrollment(100)
        .location("강남역 D2 스타트업 팩토리")
        .build();

    this.mockMvc.perform(post("/api/events")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(eventDto))
    )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].objectName").exists())
        .andExpect(jsonPath("$[0].defaultMessage").exists())
        .andExpect(jsonPath("$[0].code").exists())
    ;
  }
}
