/*
 * Copyright 2014 Christophe Pollet
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

package net.cpollet.jixture.io;

import net.cpollet.jixture.utils.ExceptionUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Christophe Pollet
 */
public class InputStreamUtils {
	private static final String CLASSPATH_MARKER = "classpath:";

	public static InputStream getInputStream(String filePath) {
		if (filePath.startsWith(CLASSPATH_MARKER)) {
			return openFileFromClasspath(filePath);
		}

		return openFileFromPath(filePath);
	}

	private static InputStream openFileFromPath(String filePath) {
		try {
			return new FileInputStream(filePath);
		}
		catch (FileNotFoundException e) {
			throw ExceptionUtils.wrapInRuntimeException(e);
		}
	}

	private static InputStream openFileFromClasspath(String classpath) {
		InputStream fileInputStream = InputStreamUtils.class.getClassLoader().getResourceAsStream(removeClasspathMarker(classpath));

		if (fileInputStream == null) {
			throw new RuntimeException("Unable to load file " + classpath);
		}

		return fileInputStream;
	}

	private static String removeClasspathMarker(String classpath) {
		return classpath.replaceFirst(CLASSPATH_MARKER, "");
	}


}
