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

import java.util.Arrays;
import java.util.List;

/**
 * Base Excel fixture.
 *
 * @see net.cpollet.jixture.fixtures.XlsFileFixture
 * @see net.cpollet.jixture.fixtures.XlsxFileFixture
 *
 * @author Christophe Pollet
 */
public abstract class ExcelFileFixture extends FileFixture {
	public enum Mode {
		IN("in", "all"),//
		OUT("out", "all"),//
		ALL("all", "in", "out");

		private List<String> markers;

		private Mode(String... markers) {
			this.markers = Arrays.asList(markers);
		}

		public List<String> getMarkers() {
			return markers;
		}
	}

	private Mode mode;

	public ExcelFileFixture(String filePath, Mode mode) {
		super(filePath);
		this.mode = mode;
	}

	public ExcelFileFixture(String filePath) {
		this(filePath, Mode.IN);
	}

	public Mode getMode() {
		return mode;
	}
}
