package jports.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import jports.reflection.Aspect;
import jports.reflection.AspectMemberAccessor;

public class FixedLengthAspect<T> extends Aspect<T, FixedLengthAspectMember<T>> {

	private Charset charset;
	private int offset;

	@Override
	protected void intializeDataType(Class<T> dataType) {
		FixedLengthTable fixedLengthTable = dataType.getAnnotation(FixedLengthTable.class);
		if (fixedLengthTable == null) {
			this.charset = Charset.defaultCharset();
			this.offset = 0;
		} else {
			this.charset = fixedLengthTable.charset().isEmpty()
					? Charset.defaultCharset()
					: Charset.forName(fixedLengthTable.charset());
			this.offset = fixedLengthTable.offset();
		}
		super.intializeDataType(dataType);
	}

	public FixedLengthAspect(Class<T> dataType) {
		super(dataType);
	}

	@Override
	protected FixedLengthAspectMember<T> visit(AspectMemberAccessor<T> accessor) {
		FixedLengthColumn annotation = accessor.getAnnotation(FixedLengthColumn.class);
		return annotation == null
				? null
				: new FixedLengthAspectMember<>(accessor, offset, annotation);
	}

	public Charset getCharset() {
		return this.charset;
	}

	public FixedLengthAspect<T> setCharset(Charset cs) {
		this.charset = cs;
		return this;
	}

	public T parseLine(String line) {
		T entity = newInstance();
		for (FixedLengthAspectMember<T> member : this) {
			member.parseAndApply(line, entity);
		}
		return entity;
	}

	public void parse(Reader in, List<T> target) throws IOException {
		BufferedReader buff = new BufferedReader(in);
		String line;
		while ((line = buff.readLine()) != null) {
			target.add(parseLine(line));
		}
	}

	public List<T> parse(InputStream is) throws IOException {
		List<T> list = new ArrayList<>();
		parse(new InputStreamReader(is, charset), list);
		return list;
	}

	public List<T> parse(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file)) {
			return parse(fis);
		}
	}

	public List<T> parse(URL url) throws IOException {
		try (InputStream us = url.openStream()) {
			return parse(us);
		}
	}

}
