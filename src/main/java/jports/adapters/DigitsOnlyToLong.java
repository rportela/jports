package jports.adapters;

public class DigitsOnlyToLong implements Adapter<Long> {

	@Override
	public Long parse(String source) {
		String v = new DigitsOnlyAdapter().parse(source);
		return v == null || v.isEmpty()
				? null
				: Long.valueOf(v);
	}

	@Override
	public String format(Long source) {
		return source == null
				? null
				: source.toString();
	}

	@Override
	public Long convert(Object source) {
		String v = new DigitsOnlyAdapter().convert(source);
		return v == null || v.isEmpty()
				? null
				: Long.valueOf(v);
	}

	@Override
	public Class<Long> getDataType() {
		return Long.class;
	}

}
