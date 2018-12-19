package me.ssoon.demoinflearnrestapi.accounts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class AccountSerializer extends JsonSerializer<Account> {

  @Override
  public void serialize(final Account account, final JsonGenerator jsonGenerator,
      final SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("id", account.getId());
    jsonGenerator.writeEndObject();
  }
}
