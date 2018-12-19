package me.ssoon.demoinflearnrestapi.events;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.*;

import java.net.URI;
import java.util.Optional;
import javax.validation.Valid;
import me.ssoon.demoinflearnrestapi.accounts.Account;
import me.ssoon.demoinflearnrestapi.accounts.CurrentUser;
import me.ssoon.demoinflearnrestapi.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

  private final EventRepository eventRepository;
  private final ModelMapper modelMapper;
  private final EventValidator eventValidator;

  public EventController(final EventRepository eventRepository, final ModelMapper modelMapper,
      final EventValidator eventValidator) {
    this.eventRepository = eventRepository;
    this.modelMapper = modelMapper;
    this.eventValidator = eventValidator;
  }

  @PostMapping
  public ResponseEntity createEvent(final @RequestBody @Valid EventDto eventDto,
      final Errors errors, final @CurrentUser Account currentUser) {
    if (errors.hasErrors()) {
      return badRequest(errors);
    }
    eventValidator.validate(eventDto, errors);
    if (errors.hasErrors()) {
      return badRequest(errors);
    }
    final Event event = modelMapper.map(eventDto, Event.class);
    event.update();
    event.setManager(currentUser);
    final Event newEvent = this.eventRepository.save(event);
    final ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
    final URI createdUri = selfLinkBuilder.toUri();
    final EventResource eventResource = new EventResource(event);
    eventResource.add(linkTo(EventController.class).withRel("query-events"));
    eventResource.add(selfLinkBuilder.withRel("update-event"));
    eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
    return ResponseEntity.created(createdUri).body(eventResource);
  }

  @GetMapping
  public ResponseEntity queryEvents(final Pageable pageable,
      final PagedResourcesAssembler<Event> assembler,
      final @CurrentUser Account account) {
    final Page<Event> page = this.eventRepository.findAll(pageable);
    final var pagedResources = assembler.toResource(page, event -> new EventResource(event));
    pagedResources.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
    if (account != null) {
      pagedResources.add(linkTo(EventController.class).withRel("create-event"));
    }
    return ResponseEntity.ok(pagedResources);
  }

  @GetMapping("/{id}")
  public ResponseEntity getEvent(final @PathVariable Integer id,
      final @CurrentUser Account currentUser) {
    final Optional<Event> optionalEvent = this.eventRepository.findById(id);
    if (optionalEvent.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    final Event event = optionalEvent.get();
    final EventResource eventResource = new EventResource(event);
    eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
    if (event.getManager().equals(currentUser)) {
      eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
    }
    return ResponseEntity.ok(eventResource);
  }

  @PutMapping("/{id}")
  public ResponseEntity updateEvent(final @PathVariable Integer id,
      final @RequestBody @Valid EventDto eventDto, final Errors errors,
      final @CurrentUser Account currentUser) {
    final Optional<Event> optionalEvent = this.eventRepository.findById(id);
    if (optionalEvent.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    if (errors.hasErrors()) {
      return badRequest(errors);
    }
    this.eventValidator.validate(eventDto, errors);
    if (errors.hasErrors()) {
      return badRequest(errors);
    }
    final Event existingEvent = optionalEvent.get();
    if (!existingEvent.getManager().equals(currentUser)) {
      return new ResponseEntity(UNAUTHORIZED);
    }
    this.modelMapper.map(eventDto, existingEvent);
    final Event savedEvent = this.eventRepository.save(existingEvent);

    final EventResource eventResource = new EventResource(savedEvent);
    eventResource.add(new Link("/docs/index.html#resources-events-update").withRel("profile"));
    return ResponseEntity.ok(eventResource);
  }

  private ResponseEntity badRequest(final Errors errors) {
    return ResponseEntity.badRequest().body(new ErrorsResource(errors));
  }
}
