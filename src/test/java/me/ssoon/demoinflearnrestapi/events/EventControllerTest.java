package me.ssoon.demoinflearnrestapi.events;

import static me.ssoon.demoinflearnrestapi.accounts.AccountRole.ADMIN;
import static me.ssoon.demoinflearnrestapi.accounts.AccountRole.USER;
import static me.ssoon.demoinflearnrestapi.events.EventStatus.DRAFT;
import static me.ssoon.demoinflearnrestapi.events.EventStatus.PUBLISHED;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_UTF8_VALUE;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;
import me.ssoon.demoinflearnrestapi.accounts.Account;
import me.ssoon.demoinflearnrestapi.accounts.AccountRepository;
import me.ssoon.demoinflearnrestapi.accounts.AccountService;
import me.ssoon.demoinflearnrestapi.common.BaseControllerTest;
import me.ssoon.demoinflearnrestapi.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

public class EventControllerTest extends BaseControllerTest {

  @Autowired
  private EventRepository eventRepository;

  @Autowired
  private AccountService accountService;

  @Autowired
  private AccountRepository accountRepository;

  @Before
  public void setUp() {
    this.eventRepository.deleteAll();
    this.accountRepository.deleteAll();
  }

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
            .header(AUTHORIZATION, getBearerToken())
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
            relaxedResponseFields(
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

  private String getBearerToken() throws Exception {
    return "Bearer " + getAccessToken();
  }

  private String getAccessToken() throws Exception {
    // Given
    final String username = "jjinimania@email.com";
    final String password = "soohoon";
    final Account account = Account.builder()
        .email(username)
        .password(password)
        .roles(Set.of(ADMIN, USER))
        .build();
    this.accountService.saveAccount(account);

    final String clientId = "myApp";
    final String clientSecret = "pass";

    final ResultActions perform = this.mockMvc.perform(post("/oauth/token")
        .with(httpBasic(clientId, clientSecret))
        .param("username", username)
        .param("password", password)
        .param("grant_type", "password")
    );
    final var responseBody = perform.andReturn().getResponse().getContentAsString();
    final Jackson2JsonParser parser = new Jackson2JsonParser();
    return parser.parseMap(responseBody).get("access_token").toString();
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
            .header(AUTHORIZATION, getBearerToken())
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
        .header(AUTHORIZATION, getBearerToken())
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
        .header(AUTHORIZATION, getBearerToken())
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

  @Test
  @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
  public void queryEvents() throws Exception {
    // Given
    IntStream.range(0, 30).forEach(this::generateEvent);

    // When & Then
    this.mockMvc.perform(get("/api/events")
        .param("page", "1")
        .param("size", "10")
        .param("sort", "name,DESC")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("page").exists())
        .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
        .andExpect(jsonPath("_links.self").exists())
        .andExpect(jsonPath("_links.profile").exists())
        .andDo(document("query-events"))
    ;
  }

  @Test
  @TestDescription("기존의 이벤트를 하나 조회하기")
  public void getEvent() throws Exception {
    // Given
    final Event event = this.generateEvent(100);

    // When & Then
    this.mockMvc.perform(get("/api/events/{id}", event.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").exists())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("_links.self").exists())
        .andExpect(jsonPath("_links.profile").exists())
        .andDo(document("get-an-event"))
    ;
  }

  @Test
  @TestDescription("없는 이벤트를 조회했을 때 404 응답받기")
  public void getEvent404() throws Exception {
    // When & Then
    this.mockMvc.perform(get("/api/events/11883"))
        .andExpect(status().isNotFound())
    ;
  }

  @Test
  @TestDescription("이벤트를 정상적으로 수정하기")
  public void updateEvent() throws Exception {
    // Given
    final Event event = this.generateEvent(200);
    final EventDto eventDto = this.modelMapper.map(event, EventDto.class);
    final String eventName = "Updated Event";
    eventDto.setName(eventName);

    // When & Then
    this.mockMvc.perform(put("/api/events/{id}", event.getId())
        .header(AUTHORIZATION, getBearerToken())
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(this.objectMapper.writeValueAsString(eventDto))
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").value(eventName))
        .andExpect(jsonPath("_links.self").exists())
        .andDo(document("update-event"))
    ;
  }

  @Test
  @TestDescription("입력값이 비어있는 경우에 이벤트 수정 실패")
  public void updateEvent400Empty() throws Exception {
    // Given
    final Event event = this.generateEvent(200);
    final EventDto eventDto = new EventDto();

    // When & Then
    this.mockMvc.perform(put("/api/events/{id}", event.getId())
        .header(AUTHORIZATION, getBearerToken())
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(this.objectMapper.writeValueAsString(eventDto))
    )
        .andDo(print())
        .andExpect(status().isBadRequest())
    ;
  }

  @Test
  @TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패")
  public void updateEvent400Wrong() throws Exception {
    // Given
    final Event event = this.generateEvent(200);
    final EventDto eventDto = this.modelMapper.map(event, EventDto.class);
    eventDto.setBasePrice(20000);
    eventDto.setMaxPrice(1000);

    // When & Then
    this.mockMvc.perform(put("/api/events/{id}", event.getId())
        .header(AUTHORIZATION, getBearerToken())
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(this.objectMapper.writeValueAsString(eventDto))
    )
        .andDo(print())
        .andExpect(status().isBadRequest())
    ;
  }

  @Test
  @TestDescription("존재하지 않는 이벤트 수정 실패")
  public void updateEvent404() throws Exception {
    // Given
    final Event event = this.generateEvent(200);
    final EventDto eventDto = this.modelMapper.map(event, EventDto.class);

    // When & Then
    this.mockMvc.perform(put("/api/events/123123")
        .header(AUTHORIZATION, getBearerToken())
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(this.objectMapper.writeValueAsString(eventDto))
    )
        .andDo(print())
        .andExpect(status().isNotFound())
    ;
  }

  private Event generateEvent(final int index) {
    final Event event = Event.builder()
        .name("event " + index)
        .description("test event")
        .beginEnrollmentDateTime(LocalDateTime.of(2018, 12, 4, 12, 17))
        .closeEnrollmentDateTime(LocalDateTime.of(2018, 12, 5, 12, 17))
        .beginEventDateTime(LocalDateTime.of(2018, 12, 6, 12, 17))
        .endEventDateTime(LocalDateTime.of(2018, 12, 7, 12, 17))
        .basePrice(100)
        .maxPrice(200)
        .limitOfEnrollment(100)
        .location("강남역 D2 스타트업 팩토리")
        .free(false)
        .offline(true)
        .eventStatus(DRAFT)
        .build();
    return this.eventRepository.save(event);
  }
}
