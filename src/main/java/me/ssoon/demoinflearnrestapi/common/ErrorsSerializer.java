package me.ssoon.demoinflearnrestapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

  @Override
  public void serialize(final Errors errors, final JsonGenerator jsonGenerator,
      final SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartArray();
    errors.getFieldErrors().forEach(e -> {
      try {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("field", e.getField());
        jsonGenerator.writeStringField("objectName", e.getObjectName());
        jsonGenerator.writeStringField("code", e.getCode());
        jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
        final Object rejectedValue = e.getRejectedValue();
        if (rejectedValue != null) {
          jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());
        }
        jsonGenerator.writeEndObject();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    errors.getGlobalErrors().forEach(e -> {
      try {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("objectName", e.getObjectName());
        jsonGenerator.writeStringField("code", e.getCode());
        jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
        jsonGenerator.writeEndObject();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    jsonGenerator.writeEndArray();
  }
}
