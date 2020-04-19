package jports.actions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateTimeISOSerializer extends JsonSerializer<LocalDateTime> {

	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// BUG: The standard ISO_DATE_TIME is returning null for the entire
		// serialization;
		gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "+0000");

	}

}