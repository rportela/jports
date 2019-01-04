package jports.adapters;

import jports.ShowStopper;

public class DoubleDiv implements Adapter<Double> {

	private final double divBy;

	public DoubleDiv(double divideBy) {
		this.divBy = divideBy;
	}

	@Override
	public Double parse(String source) {
		return source == null || source.isEmpty()
				? 0.0
				: Double.parseDouble(source) / divBy;
	}

	@Override
	public String format(Double source) {
		return source == null
				? ""
				: Double.toString(source * divBy);
	}

	@Override
	public Double convert(Object source) {
		if (source == null)
			return null;
		else if (source instanceof Number)
			return ((Number) source).doubleValue() / divBy;
		else if (source instanceof String)
			return parse((String) source);
		else
			throw new ShowStopper("Unable to parse to double div by " + divBy + " -> " + source);
	}

	@Override
	public Class<Double> getDataType() {
		return Double.class;
	}

}
