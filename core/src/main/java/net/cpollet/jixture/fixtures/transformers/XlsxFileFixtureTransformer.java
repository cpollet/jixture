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

package net.cpollet.jixture.fixtures.transformers;

import net.cpollet.jixture.fixtures.XlsxFileFixture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Christophe Pollet
 */
public class XlsxFileFixtureTransformer extends ExcelFileFixtureTransformer<XlsxFileFixture> {
	@Override
	public Class getFromType() {
		return XlsxFileFixture.class;
	}

	@Override
	protected Workbook createWorkbook(InputStream inputStream) {
		try {
			return new XSSFWorkbook(inputStream);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
