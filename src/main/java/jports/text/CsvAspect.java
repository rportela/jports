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

	private String separator;
	private Charset charset;
	private String commentQualifier;
	private int capacity;
	private boolean firstRowHasNames;

	public CsvAspect(Class<TClass> claz) {
		super(claz);

		CsvTable table = claz.getAnnotation(CsvTable.class);
		if (table == null) {
			this.separator = ",";
			this.charset = Charset.defaultCharset();
			this.commentQualifier = null;
			this.capacity = 100;
			this.firstRowHasNames = true;
		} else {
			this.separator = table.separator();
			this.commentQualifier = table.commentQualifier();
			this.charset = table.charset().isEmpty()
					? Charset.defaultCharset()
					: Charset.forName(table.charset());
			this.capacity = table.capacity();
			this.firstRowHasNames = table.firstRowHasNames();
		}

	}

	public String getSeparator() {
		return this.separator;
	}

	public CsvAspect<TClass> setSeparator(String separator) {
		this.separator = separator;
		return this;
	}

	public Charset getCharset() {
		return this.charset;
	}

	public CsvAspect<TClass> setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	public CsvAspect<TClass> setCharset(String charsetName) {
		this.charset = Charset.forName(charsetName);
		return this;
	}

	public String getCommentQualifier() {
		return this.commentQualifier;
	}

	public CsvAspect<TClass> setCommentQualifier(String commentQualifier) {
		this.commentQualifier = commentQualifier;
		return this;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public CsvAspect<TClass> setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	public boolean firstRowHasNames() {
		return this.firstRowHasNames;
	}

	public CsvAspect<TClass> firstRowHasNames(boolean value) {
		this.firstRowHasNames = value;
		return this;
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

	public List<TClass> parse(InputStream source) {
		List<TClass> list = new ArrayList<>(capacity);
		parse(source, list);
		return list;
	}

	public List<TClass> parse(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		try {
			return parse(fis);
		} finally {
			fis.close();
		}
	}

	public List<TClass> parse(URL url) throws IOException {
		InputStream fis = url.openStream();
		try {
			return parse(fis);
		} finally {
			fis.close();
		}
	}

}
