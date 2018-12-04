package me.ssoon.demoinflearnrestapi.events;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class EventTest {

  @Test
  public void builder() {
    final Event event = Event.builder()
        .name("Inflearn Spring REST API")
        .description("REST API development with Spring")
        .build();
    assertThat(event).isNotNull();
  }

  @Test
  public void javaBean() {
    // Given
    final String name = "Event";
    final String description = "Spring";

    // When
    final Event event = new Event();
    event.setName(name);
    event.setDescription(description);

    // Then
    assertThat(event.getName()).isEqualTo(name);
    assertThat(event.getDescription()).isEqualTo(description);
  }
}