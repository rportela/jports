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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jports.ShowStopper;
import jports.reflection.Aspect;
import jports.reflection.AspectMemberAccessor;

/**
 * This class accesses CSV annotations on classes and members besides providing
 * useful methods for reading CSV files;
 * 
 * @author rportela
 *
 * @param <T>
 */
public class CsvAspect<T> extends Aspect<T, CsvAspectMember<T>> {

	private String separator;
	private Charset charset;
	private String commentQualifier;
	private int capacity;
	private boolean firstRowHasNames;
	private String linePrefix;

	public CsvAspect(Class<T> claz) {
		super(claz);

		CsvTable table = claz.getAnnotation(CsvTable.class);
		if (table == null) {
			this.separator = ",";
			this.charset = Charset.defaultCharset();
			this.commentQualifier = null;
			this.capacity = 100;
			this.firstRowHasNames = true;
			this.linePrefix = "";
		} else {
			this.separator = table.separator();
			this.commentQualifier = table.commentQualifier();
			this.charset = table.charset().isEmpty()
					? Charset.defaultCharset()
					: Charset.forName(table.charset());
			this.capacity = table.capacity();
			this.firstRowHasNames = table.firstRowHasNames();
			this.linePrefix = table.linePrefix();
		}

	}

	public String getSeparator() {
		return this.separator;
	}

	public CsvAspect<T> setSeparator(String separator) {
		this.separator = separator;
		return this;
	}

	public Charset getCharset() {
		return this.charset;
	}

	public CsvAspect<T> setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	public CsvAspect<T> setCharset(String charsetName) {
		this.charset = Charset.forName(charsetName);
		return this;
	}

	public String getCommentQualifier() {
		return this.commentQualifier;
	}

	public CsvAspect<T> setCommentQualifier(String commentQualifier) {
		this.commentQualifier = commentQualifier;
		return this;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public CsvAspect<T> setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	public boolean firstRowHasNames() {
		return this.firstRowHasNames;
	}

	public CsvAspect<T> firstRowHasNames(boolean value) {
		this.firstRowHasNames = value;
		return this;
	}

	@Override
	protected CsvAspectMember<T> visit(AspectMemberAccessor<T> accessor) {
		CsvColumn csv = accessor.getAnnotation(CsvColumn.class);
		return csv == null
				? null
				: new CsvAspectMember<>(accessor, csv, super.size());
	}

	public void parse(
			final InputStream source,
			final List<T> target) {
		String line;
		boolean frowhasnames = this.firstRowHasNames;
		try {
			Constructor<T> constructor = super.getDataType().getConstructor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(source, charset));
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty() ||
						(commentQualifier != null &&
								!commentQualifier.isEmpty() &&
								line.startsWith(commentQualifier))) {
					// just ignore
				} else if (frowhasnames) {
					frowhasnames = false;
					String[] names = line.split(separator);
					for (CsvAspectMember<?> member : this)
						member.setPositionFrom(names);
				} else if (linePrefix.isEmpty() || line.startsWith(linePrefix)) {
					String[] cells = line.split(separator);
					T entity = constructor.newInstance();
					for (CsvAspectMember<T> member : this)
						member.parseAndApply(cells, entity);
					target.add(entity);
				}
			}
		} catch (Exception e) {
			throw new ShowStopper(e);
		}

	}

	public List<T> parse(InputStream source) {
		List<T> list = new ArrayList<>(capacity);
		parse(source, list);
		return list;
	}

	public List<T> parse(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return parse(fis);
		}
	}

	public List<T> parse(URL url) throws IOException {
		try (InputStream fis = url.openStream()) {
			return parse(fis);
		}
	}

	public Map<String, List<T>> parseZip(InputStream source) throws IOException {
		ZipEntry entry;
		try (ZipInputStream zis = new ZipInputStream(source)) {
			LinkedHashMap<String, List<T>> map = new LinkedHashMap<>(12);
			while ((entry = zis.getNextEntry()) != null) {
				String name = entry.getName();
				List<T> list = parse(zis);
				map.put(name, list);
			}
			return map;
		}
	}

	public Map<String, List<T>> parseZip(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return parseZip(fis);
		}
	}

	public Map<String, List<T>> parseZip(URL url) throws IOException {
		try (InputStream fis = url.openStream()) {
			return parseZip(fis);
		}
	}

}
