package me.ssoon.demoinflearnrestapi.events;

import static me.ssoon.demoinflearnrestapi.events.EventStatus.DRAFT;
import static me.ssoon.demoinflearnrestapi.events.EventStatus.PUBLISHED;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_UTF8_VALUE;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import me.ssoon.demoinflearnrestapi.common.RestDocsConfiguration;
import me.ssoon.demoinflearnrestapi.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
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
        .andExpect(jsonPath("free").value(false))
        .andExpect(jsonPath("offline").value(true))
        .andExpect(jsonPath("eventStatus").value(DRAFT.name()))
        .andDo(document("create-event",
            links(
                linkWithRel("self").description("Link to self"),
                linkWithRel("query-events").description("Link to query events"),
                linkWithRel("update-event").description("Link to update an existing event"),
                linkWithRel("profile").description("Link to profile")
            ),
            requestHeaders(
                headerWithName(ACCEPT).description("Accept header"),
                headerWithName(CONTENT_TYPE).description("Content type header")
            ),
            requestFields(
                fieldWithPath("name").description("Name of new event"),
                fieldWithPath("description").description("Description of new event"),
                fieldWithPath("beginEnrollmentDateTime").description("Date time of begin enrollment of new event"),
                fieldWithPath("closeEnrollmentDateTime").description("Date time of close enrollment of new event"),
                fieldWithPath("beginEventDateTime").description("Date time of begin of new event"),
                fieldWithPath("endEventDateTime").description("Date time of end of new event"),
                fieldWithPath("location").description("Location of new event"),
                fieldWithPath("basePrice").description("Base price of new event"),
                fieldWithPath("maxPrice").description("Max price of new event"),
                fieldWithPath("limitOfEnrollment").description("Limit of enrollment")
            ),
            responseHeaders(
                headerWithName(LOCATION).description("Location header"),
                headerWithName(CONTENT_TYPE).description("Content type header")
            ),
            responseFields(
                fieldWithPath("id").description("Identifier of new event"),
                fieldWithPath("name").description("Name of new event"),
                fieldWithPath("description").description("Description of new event"),
                fieldWithPath("beginEnrollmentDateTime").description("Date time of begin enrollment of new event"),
                fieldWithPath("closeEnrollmentDateTime").description("Date time of close enrollment of new event"),
                fieldWithPath("beginEventDateTime").description("Date time of begin of new event"),
                fieldWithPath("endEventDateTime").description("Date time of end of new event"),
                fieldWithPath("location").description("Location of new event"),
                fieldWithPath("basePrice").description("Base price of new event"),
                fieldWithPath("maxPrice").description("Max price of new event"),
                fieldWithPath("limitOfEnrollment").description("Limit of enrollment"),
                fieldWithPath("free").description("It tells if this event is free or not"),
                fieldWithPath("offline").description("It tells if this event is offline event or not"),
                fieldWithPath("eventStatus").description("Event status"),
                fieldWithPath("_links.self.href").description("Link to self"),
                fieldWithPath("_links.query-events.href").description("Link to query event list"),
                fieldWithPath("_links.update-event.href").description("Link to update existing event"),
                fieldWithPath("_links.profile.href").description("Link to profile")
            )
        ))
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
        .andExpect(jsonPath("content[0].objectName").exists())
        .andExpect(jsonPath("content[0].defaultMessage").exists())
        .andExpect(jsonPath("content[0].code").exists())
        .andExpect(jsonPath("_links.index").exists())
    ;
  }
}
