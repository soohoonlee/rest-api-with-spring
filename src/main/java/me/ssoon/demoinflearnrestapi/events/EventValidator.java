package me.ssoon.demoinflearnrestapi.events;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {

  private static final String WRONG_VALUE = "wrongValue";

  public void validate(final EventDto eventDto, final Errors errors) {
    if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
      errors.reject("wrongPrices", "Values for prices are wrong.");
    }
    final @NotNull LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
    final @NotNull LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
    final @NotNull LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
    final @NotNull LocalDateTime beginEnrollmentDateTime = eventDto.getBeginEnrollmentDateTime();
    if (endEventDateTime.isBefore(beginEventDateTime) ||
        endEventDateTime.isBefore(closeEnrollmentDateTime) ||
        endEventDateTime.isBefore(beginEnrollmentDateTime)) {
      errors.rejectValue("endEventDateTime", WRONG_VALUE, "endEventDateTime is wrong.");
    }
    if (beginEventDateTime.isBefore(closeEnrollmentDateTime) ||
        beginEventDateTime.isBefore(beginEnrollmentDateTime)) {
      errors.rejectValue("beginEventDateTime", WRONG_VALUE, "beginEventDateTime is wrong.");
    }
    if (closeEnrollmentDateTime.isBefore(beginEnrollmentDateTime)) {
      errors.rejectValue("closeEnrollmentDateTime", WRONG_VALUE, "closeEnrollmentDateTime is wrong.");
    }
  }
}
