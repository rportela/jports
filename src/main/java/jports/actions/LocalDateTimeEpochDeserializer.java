package jports.actions;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

public class LocalDateTimeEpochDeserializer extends LocalDateTimeDeserializer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -584271379508134210L;

	public LocalDateTimeEpochDeserializer() {
		super(DateTimeFormatter.ISO_DATE_TIME);
	}

	@Override
	public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
		if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
			long value = parser.getValueAsLong();
			Instant instant = Instant.ofEpochMilli(value);
			return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
		}
		return super.deserialize(parser, context);
	}

}