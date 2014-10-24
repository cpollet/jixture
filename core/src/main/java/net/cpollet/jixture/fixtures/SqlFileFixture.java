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
import net.cpollet.jixture.fixtures.capacities.cleaning.CleanableFixture;
import net.cpollet.jixture.utils.InputStreamUtils;
import net.cpollet.jixture.utils.ExceptionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * Loads data from a SQL file.
 *
 * @author Christophe Pollet
 */
public class SqlFileFixture implements RawFixture, CleanableFixture {
	private CleaningFixture cleaningFixture;
	private InputStream fileInputStream;

	/**
	 * @param filePath the path to the SQL file. If the path starts with {@code classpath:/}, the path is relative
	 *                 the classpath's root. Otherwise, it's considered a filesystem path.
	 */
	public SqlFileFixture(String filePath) {
		this.cleaningFixture = new CleaningFixture();
		this.fileInputStream = InputStreamUtils.getInputStream(filePath);
	}

	/**
	 * @param filePath the path to the SQL file. If the path starts with {@code classpath:/}, the path is relative
	 *                 the classpath's root. Otherwise, it's considered a filesystem path.
	 * @param classes the mapping classes representing tge tables to truncate before executing the SQL commands.
	 */
	public SqlFileFixture(String filePath, Class... classes) {
		this.cleaningFixture = new CleaningFixture(classes);
		this.fileInputStream = InputStreamUtils.getInputStream(filePath);
	}

	/**
	 * Execute the SQL file's content.
	 *
	 * @param unitDaoFactory the {@link net.cpollet.jixture.dao.UnitDaoFactory} to use.
	 */
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
		return 0 == line.length() || line.startsWith("--");
	}

	private boolean isEndOfQuery(String line) {
		return line.endsWith(";");
	}

	/**
	 * Returns an ordered iterator of mapping classes to delete from database.
	 *
	 * @return an ordered iterator of mapping classes to delete from database.
	 */
	@Override
	public Iterator<Class> getClassesToDeleteIterator() {
		return cleaningFixture.getClassesToDeleteIterator();
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
			return null != currentLine;
		}

		@Override
		public String next() {
			String currentLine = this.currentLine;
			this.currentLine = readNextLine();
			return currentLine;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
