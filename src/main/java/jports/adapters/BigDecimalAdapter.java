package jports.adapters;

import java.math.BigDecimal;

import jports.ShowStopper;

public class BigDecimalAdapter implements Adapter<BigDecimal> {

	@Override
	public BigDecimal parse(String source) {
		return source == null || source.isEmpty()
				? null
				: new BigDecimal(source);
	}

	@Override
	public String format(BigDecimal source) {
		return source == null
				? null
				: source.toString();
	}

	@Override
	public BigDecimal convert(Object source) {
		if (source == null)
			return null;
		else if (source instanceof BigDecimal)
			return ((BigDecimal) source);
		else if (source instanceof Number)
			return BigDecimal.valueOf(((Number) source).doubleValue());
		else if (source instanceof String)
			return parse((String) source);
		else
			throw new ShowStopper("Can't convert to BigDecimal: " + source);
	}

	@Override
	public Class<BigDecimal> getDataType() {
		return BigDecimal.class;
	}

}
