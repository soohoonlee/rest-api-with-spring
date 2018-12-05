package me.ssoon.demoinflearnrestapi.events;

import static org.assertj.core.api.Assertions.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
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

  @Test
  @Parameters
  public void testFree(final int basePrice, final int maxPrice, final boolean isFree) {
    // Given
    final Event event = Event.builder()
        .basePrice(basePrice)
        .maxPrice(maxPrice)
        .build();

    // When
    event.update();

    // Then
    assertThat(event.isFree()).isEqualTo(isFree);
  }

  private Object[] parametersForTestFree() {
    return new Object[] {
        new Object[] {0, 0, true},
        new Object[] {100, 0, false},
        new Object[] {0, 100, false},
        new Object[] {100, 200, false}
    };
  }

  @Test
  @Parameters
  public void testOffline(final String location, final boolean isOffline) {
    // Given
    final Event event = Event.builder()
        .location(location)
        .build();

    // When
    event.update();

    // Then
    assertThat(event.isOffline()).isEqualTo(isOffline);
  }

  private Object[] parametersForTestOffline() {
    return new Object[] {
        new Object[] {"강남", true},
        new Object[] {null, false},
        new Object[] {" ", false},
    };
  }
}