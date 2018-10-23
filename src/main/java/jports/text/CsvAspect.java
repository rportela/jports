package jports.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import jports.reflection.Aspect;
import jports.reflection.AspectMemberAccessor;

/**
 * This class accesses CSV annotations on classes and members besides providing
 * useful methods for reading CSV files;
 * 
 * @author rportela
 *
 * @param <TClass>
 */
public class CsvAspect<TClass> extends Aspect<TClass, CsvAspectMember<TClass>> {

	public final String default_separator;
	public final Charset default_charset;
	public final String default_commentQualifier;
	public final int default_capacity;
	public final boolean first_row_has_names;

	public CsvAspect(Class<TClass> claz) {
		super(claz);

		CsvTable table = claz.getAnnotation(CsvTable.class);
		if (table == null) {
			this.default_separator = ",";
			this.default_charset = Charset.defaultCharset();
			this.default_commentQualifier = null;
			this.default_capacity = 100;
			this.first_row_has_names = true;
		} else {
			this.default_separator = table.separator();
			this.default_commentQualifier = table.commentQualifier();
			this.default_charset = table.charset().isEmpty()
					? Charset.defaultCharset()
					: Charset.forName(table.charset());
			this.default_capacity = table.capacity();
			this.first_row_has_names = table.firstRowHasNames();
		}

	}

	@Override
	protected CsvAspectMember<TClass> visit(AspectMemberAccessor<TClass> accessor) {
		CsvColumn csv = accessor.getAnnotation(CsvColumn.class);
		return csv == null
				? null
				: new CsvAspectMember<>(accessor, csv, super.size());
	}

	public void parse(
			InputStream source,
			Charset charset,
			String separator,
			boolean firstRowHasNames,
			String commentQualifier,
			List<TClass> target) {
		String line;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(source, charset))) {
			Constructor<TClass> constructor = super.getDataType().getConstructor();
			while ((line = reader.readLine()) != null) {
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

	public List<TClass> parse(
			InputStream source,
			Charset charset,
			String separator,
			boolean firstRowHasNames,
			int initialCapacity,
			String commentQualifier) {

		List<TClass> list = new ArrayList<>(initialCapacity);
		parse(
				source,
				charset,
				separator,
				firstRowHasNames,
				commentQualifier,
				list);
		return list;
	}

	public List<TClass> parse(InputStream source, Charset charset, String separator, boolean firstRowHasNames) {
		return parse(source, charset, separator, firstRowHasNames, default_capacity, default_commentQualifier);
	}

	public List<TClass> parse(InputStream source, Charset charset, String separator) {
		return parse(source, charset, separator, first_row_has_names, default_capacity, default_commentQualifier);
	}

	public List<TClass> parse(InputStream source, Charset charset) {
		return parse(
				source,
				charset,
				default_separator,
				first_row_has_names,
				default_capacity,
				default_commentQualifier);
	}

	public List<TClass> parse(InputStream source) {
		return parse(
				source,
				default_charset,
				default_separator,
				first_row_has_names,
				default_capacity,
				default_commentQualifier);
	}

	public List<TClass> parse(File file, Charset charset, String separator, boolean firstRowHasNames)
			throws IOException {
		FileInputStream fis = new FileInputStream(file);
		try {
			return parse(
					fis,
					charset,
					separator,
					firstRowHasNames,
					default_capacity,
					default_commentQualifier);
		} finally {
			fis.close();
		}
	}

	public List<TClass> parse(File file, Charset charset, String separator) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		try {
			return parse(
					fis,
					charset,
					separator,
					first_row_has_names,
					default_capacity,
					default_commentQualifier);
		} finally {
			fis.close();
		}

	}

	public List<TClass> parse(File file, Charset charset) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		try {
			return parse(
					fis,
					charset,
					default_separator,
					first_row_has_names,
					default_capacity,
					default_commentQualifier);
		} finally {
			fis.close();
		}
	}

	public List<TClass> parse(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		try {
			return parse(
					fis,
					default_charset,
					default_separator,
					first_row_has_names,
					default_capacity,
					default_commentQualifier);
		} finally {
			fis.close();
		}
	}

	public List<TClass> parse(URL url, Charset charset, String separator, boolean firstRowHasNames)
			throws IOException {
		InputStream fis = url.openStream();
		try {
			return parse(
					fis,
					charset,
					separator,
					firstRowHasNames,
					default_capacity,
					default_commentQualifier);
		} finally {
			fis.close();
		}
	}

	public List<TClass> parse(URL url, Charset charset, String separator) throws IOException {
		InputStream fis = url.openStream();
		try {
			return parse(
					fis,
					charset,
					separator,
					first_row_has_names,
					default_capacity,
					default_commentQualifier);
		} finally {
			fis.close();
		}

	}

	public List<TClass> parse(URL url, Charset charset) throws IOException {
		InputStream fis = url.openStream();
		try {
			return parse(
					fis,
					charset,
					default_separator,
					first_row_has_names,
					default_capacity,
					default_commentQualifier);
		} finally {
			fis.close();
		}
	}

	public List<TClass> parse(URL url) throws IOException {
		InputStream fis = url.openStream();
		try {
			return parse(
					fis,
					default_charset,
					default_separator,
					first_row_has_names,
					default_capacity,
					default_commentQualifier);
		} finally {
			fis.close();
		}
	}

}
