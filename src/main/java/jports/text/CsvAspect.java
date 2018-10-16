package jports.text;

import java.io.BufferedReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import jports.reflection.Aspect;
import jports.reflection.AspectMemberAccessor;

public class CsvAspect<TClass> extends Aspect<TClass, CsvAspectMember<TClass>> {

	public CsvAspect(Class<TClass> claz) {
		super(claz);
	}

	@Override
	protected CsvAspectMember<TClass> visit(AspectMemberAccessor<TClass> accessor) {
		CsvColumn csv = accessor.getAnnotation(CsvColumn.class);
		return csv == null ? null : new CsvAspectMember<>(accessor, csv, super.size());
	}

	public void parse(BufferedReader source, List<TClass> target, String separator, boolean firstRowHasNames,
			String commentQualifier) {
		String line;
		try {
			Constructor<TClass> constructor = super.getDataType().getConstructor();
			while ((line = source.readLine()) != null) {
				if (line.isEmpty())
					continue;
				if (commentQualifier != null && !commentQualifier.isEmpty() && line.startsWith(commentQualifier))
					continue;
				if (firstRowHasNames) {
					firstRowHasNames = false;
					String[] names = line.split(separator);
					for (CsvAspectMember<?> member : this)
						member.setPositionFrom(names);
				} else {
					String[] cells = line.split(separator);
					TClass entity = constructor.newInstance();
					for (CsvAspectMember<TClass> member : this)
						member.parseAndApply(cells, entity);
					target.add(entity);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<TClass> parse(BufferedReader source, String separator, boolean firstRowHasNames, int initialCapacity,
			String commentQualifier) {
		List<TClass> list = new ArrayList<>(initialCapacity);
		parse(source, list, separator, firstRowHasNames, commentQualifier);
		return list;
	}

	public List<TClass> parse(BufferedReader source, String separator, boolean firstRowHasNames) {
		return parse(source, separator, firstRowHasNames, 100, null);
	}

}
