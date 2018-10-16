package jports.adapters;

public class DigitsOnlyToInteger implements Adapter<Integer> {

	@Override
	public Integer parse(String source) {
		String v = new DigitsOnlyAdapter().parse(source);
		return v == null || v.isEmpty()
				? null
				: Integer.valueOf(v);
	}

	@Override
	public String format(Integer source) {
		return source == null
				? null
				: source.toString();
	}

	@Override
	public Integer convert(Object source) {
		String v = new DigitsOnlyAdapter().convert(source);
		return v == null || v.isEmpty()
				? null
				: Integer.valueOf(v);
	}

	@Override
	public Class<Integer> getDataType() {
		return Integer.class;
	}

}
