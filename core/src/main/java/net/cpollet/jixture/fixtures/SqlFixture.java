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

package net.cpollet.jixture.fixtures;

import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.io.InputStreamUtils;
import net.cpollet.jixture.utils.ExceptionUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Christophe Pollet
 */
public class SqlFixture implements RawFixture {
	private CleaningFixture cleaningFixture;
	private InputStream fileInputStream;

	public SqlFixture(String filePath) {
		this.cleaningFixture = new CleaningFixture();
		this.fileInputStream = InputStreamUtils.getInputStream(filePath);
	}

	public SqlFixture(String filePath, Class... classes) {
		this.cleaningFixture = new CleaningFixture(classes);
		this.fileInputStream = InputStreamUtils.getInputStream(filePath);
	}

	@Override
	public void load(UnitDaoFactory unitDaoFactory) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
		BufferedReaderIterator iterator = new BufferedReaderIterator(reader);

		StringBuilder currentQuery = new StringBuilder();
		while (iterator.hasNext()) {
			String line = iterator.next().trim();

			if (!isCommentOrEmptyLine(line)) {
				currentQuery.append(line).append(" ");
			}

			if (isEndOfQuery(line)) {
				unitDaoFactory.getUnitDao().execute(currentQuery.toString());
				currentQuery = new StringBuilder();
			}
		}
	}

	private boolean isCommentOrEmptyLine(String line) {
		return line.length() == 0 || line.startsWith("--");
	}

	private boolean isEndOfQuery(String line) {
		return line.endsWith(";");
	}

	@Override
	public LinkedList<Class> getClassesToDelete() {
		return cleaningFixture.getClassesToDelete();
	}

	private class BufferedReaderIterator implements Iterator<String> {
		private BufferedReader bufferedReader;
		private String currentLine = null;

		public BufferedReaderIterator(BufferedReader bufferedReader) {
			this.bufferedReader = bufferedReader;
			currentLine = readNextLine();
		}

		private String readNextLine() {
			try {
				return bufferedReader.readLine();
			}
			catch (IOException e) {
				throw ExceptionUtils.wrapInRuntimeException(e);
			}
		}

		@Override
		public boolean hasNext() {
			return currentLine != null;// && nextLine != null;
		}

		@Override
		public String next() {
			String currentLine = this.currentLine;
			this.currentLine = readNextLine();
			return currentLine;
		}

		@Override
		public void remove() {
			throw new NotImplementedException();
		}
	}
}
