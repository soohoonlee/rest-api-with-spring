package me.ssoon.demoinflearnrestapi.events;

import static me.ssoon.demoinflearnrestapi.events.EventStatus.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ssoon.demoinflearnrestapi.accounts.Account;
import me.ssoon.demoinflearnrestapi.accounts.AccountSerializer;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Event {

  @Id
  @GeneratedValue
  private Integer id;
  private String name;
  private String description;
  private LocalDateTime beginEnrollmentDateTime;
  private LocalDateTime closeEnrollmentDateTime;
  private LocalDateTime beginEventDateTime;
  private LocalDateTime endEventDateTime;
  private String location;
  private int basePrice;
  private int maxPrice;
  private int limitOfEnrollment;
  private boolean offline;
  private boolean free;
  @Enumerated(EnumType.STRING)
  private EventStatus eventStatus = DRAFT;
  @ManyToOne
  @JsonSerialize(using = AccountSerializer.class)
  private Account manager;

  public void update() {
    // Update free
    this.free = this.basePrice == 0 && this.maxPrice == 0;
    // Update offline
    this.offline = this.location != null && !this.location.isBlank();
  }
}
