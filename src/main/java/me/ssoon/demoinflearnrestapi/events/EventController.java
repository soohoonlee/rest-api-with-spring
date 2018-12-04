package me.ssoon.demoinflearnrestapi.events;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.net.URI;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

  private final EventRepository eventRepository;
  private final ModelMapper modelMapper;

  public EventController(final EventRepository eventRepository, final ModelMapper modelMapper) {
    this.eventRepository = eventRepository;
    this.modelMapper = modelMapper;
  }

  @PostMapping
  public ResponseEntity createdEvent(final @RequestBody @Valid EventDto eventDto, final Errors errors) {
    if (errors.hasErrors()) {
      return ResponseEntity.badRequest().build();
    }
    final Event event = modelMapper.map(eventDto, Event.class);
    final Event newEvent = this.eventRepository.save(event);
    final URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
    return ResponseEntity.created(createdUri).body(event);
  }
}
