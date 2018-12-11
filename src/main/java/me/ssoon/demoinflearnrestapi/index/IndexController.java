package me.ssoon.demoinflearnrestapi.index;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import me.ssoon.demoinflearnrestapi.events.EventController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  @GetMapping("/api")
  public ResourceSupport index() {
    final var index = new ResourceSupport();
    index.add(linkTo(EventController.class).withRel("events"));
    return index;
  }
}
