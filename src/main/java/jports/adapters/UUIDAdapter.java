package jports.adapters;

import java.util.UUID;

public class UUIDAdapter implements Adapter<UUID> {

	@Override
	public UUID parse(String source) {
		return source == null || source.isEmpty()
				? null
				: UUID.fromString(source);
	}

	@Override
	public String format(UUID source) {
		return source == null
				? null
				: source.toString();
	}

	@Override
	public UUID convert(Object source) {
		if (source == null)
			return null;
		else if (source instanceof UUID)
			return (UUID) source;
		else if (source instanceof byte[])
			return UUID.nameUUIDFromBytes((byte[]) source);
		else if (source instanceof String)
			return UUID.fromString((String) source);
		else
			throw new RuntimeException("Can't make UUID from " + source);
	}

	@Override
	public Class<UUID> getDataType() {
		return UUID.class;
	}

}
