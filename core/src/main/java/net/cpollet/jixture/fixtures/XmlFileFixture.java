/*
 * Copyright 2013 Christophe Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

	public XmlFileFixture(String filePath) {
		if (filePath.startsWith(CLASSPATH_MARKER)) {
			fileInputStream = openFileFromClasspath(filePath);
		}
		else {
			fileInputStream = openFileFromPath(filePath);
		}
	}

	private InputStream openFileFromPath(String filePath) {
		try {
			return new FileInputStream(filePath);
		}
		catch (FileNotFoundException e) {
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
}
