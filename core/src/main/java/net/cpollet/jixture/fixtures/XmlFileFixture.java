package net.cpollet.jixture.fixtures;

import net.cpollet.jixture.utils.ExceptionUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Christophe Pollet
 */
public class XmlFileFixture implements TransformableFixture {
	private static final String CLASSPATH_MARKER = "classpath:";

	private InputStream fileInputStream;
	private FixtureTransformer fixtureTransformer;

	public XmlFileFixture(String filePath, FixtureTransformer fixtureTransformer) {
		if (filePath.startsWith(CLASSPATH_MARKER)) {
			fileInputStream = openFileFromClasspath(filePath);
		} else {
			fileInputStream = openFileFromPath(filePath);
		}

		this.fixtureTransformer = fixtureTransformer;
	}

	private InputStream openFileFromPath(String filePath) {
		try {
			return new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw ExceptionUtils.wrapInRuntimeException(e);
		}
	}

	private InputStream openFileFromClasspath(String classpath) {
		fileInputStream = getClass().getClassLoader().getResourceAsStream(removeClasspathMarker(classpath));

		if (fileInputStream == null) {
			throw new RuntimeException("Unable to load file " + classpath);
		}

		return fileInputStream;
	}

	private String removeClasspathMarker(String classpath) {
		return classpath.replaceFirst(CLASSPATH_MARKER, "");
	}

	public InputStream getInputStream() {
		return fileInputStream;
	}

	public Fixture getTransformedFixture() {
		return fixtureTransformer.transform(this);
	}
}
