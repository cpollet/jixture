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

/**
 * Loads fixture from an Excel XLSX file. This fixture is transformed into an
 * {@link net.cpollet.jixture.fixtures.ObjectFixture} using
 * {@link net.cpollet.jixture.fixtures.transformers.ExcelFileFixtureTransformer}.
 *
 * @see net.cpollet.jixture.fixtures.ObjectFixture
 * @see net.cpollet.jixture.fixtures.transformers.ExcelFileFixtureTransformer
 *
 * @author Christophe Pollet
 */
public class XlsxFileFixture extends ExcelFileFixture {
	public XlsxFileFixture(String filePath, Mode mode) {
		super(filePath, mode);
	}

	public XlsxFileFixture(String filePath) {
		super(filePath);
	}
}
