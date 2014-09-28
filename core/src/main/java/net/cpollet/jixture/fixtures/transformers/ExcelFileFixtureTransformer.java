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

import net.cpollet.jixture.fixtures.ExcelFileFixture;
import net.cpollet.jixture.helper.MappingBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Christophe Pollet
 */
public abstract class ExcelFileFixtureTransformer<From extends ExcelFileFixture> extends FileFixtureTransformer<From> {

	private static final String DEFAULT_MODE_COLUMN_NAME = "mode";
	private static final String DEFAULT_ESCAPE_CHAR = "!";

	private String modeColumnName = DEFAULT_MODE_COLUMN_NAME;
	private String escapeChar = DEFAULT_ESCAPE_CHAR;

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

	public void setModeColumnName(String modeColumnName) {
		this.modeColumnName = modeColumnName;
	}

	public void setEscapeChar(String escapeChar) {
		this.escapeChar = escapeChar;
	}

	@Override
	protected List<Object> parse(From fixture) {
		InputStream inputStream = fixture.getInputStream();
		Workbook workbook = createWorkbook(inputStream);

		DataFormatter dataFormatter = new DataFormatter();
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

		List<Object> mappings = new LinkedList<Object>();
		Helpers helpers = new Helpers();
		helpers.dataFormatter = dataFormatter;
		helpers.evaluator = evaluator;

		Parameters parameters = new Parameters();
		parameters.mode = fixture.getMode();

		for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
			Sheet sheet = workbook.getSheetAt(sheetIndex);

			mappings.addAll(parseSheet(sheet, helpers, parameters));
		}

		return mappings;
	}

	private List<Object> parseSheet(Sheet sheet, Helpers helpers, Parameters parameters) {
		parameters.tableName = sheet.getSheetName();

		if (parameters.tableName.startsWith(escapeChar)) {
			return Collections.emptyList();
		}

		List<Object> mappings = new LinkedList<Object>();

		Iterator<Row> rowIterator = sheet.iterator();

		if (rowIterator.hasNext()) {
			Row rowColumnNames = rowIterator.next();

			while (rowIterator.hasNext()) {
				Object mapping = buildMappingForRow(helpers, parameters, rowIterator.next(), rowColumnNames);

				if (null != mapping) {
					mappings.add(mapping);
				}
			}
		}

		return mappings;
	}

	@SuppressWarnings("MethodWithTooManyParameters")
	private Object buildMappingForRow(Helpers helpers, Parameters parameters, Row rowColumnValues, Row rowColumnNames) {
		MappingBuilder mappingBuilder = mappingBuilderFactory.create(parameters.tableName);

		Iterator<Cell> valuesIterator = rowColumnValues.cellIterator();
		Iterator<Cell> columnsIterator = rowColumnNames.cellIterator();

		while (valuesIterator.hasNext() && columnsIterator.hasNext()) {
			Cell columnNameCell = columnsIterator.next();
			Cell columnValueCell = valuesIterator.next();

			helpers.evaluator.evaluateFormulaCell(columnValueCell);

			String columnName = columnNameCell.getStringCellValue();
			String columnValue = helpers.dataFormatter.formatCellValue(columnValueCell, helpers.evaluator);

			if ((escapeChar + modeColumnName).equals(columnName)) {
				if (!parameters.mode.getMarkers().contains(columnValue)) {
					return null;
				}
			}
			else {
				mappingBuilder.withValue(columnName, columnValue);
			}
		}

		return mappingBuilder.build();
	}

	protected abstract Workbook createWorkbook(InputStream inputStream);

	private class Helpers {
		DataFormatter dataFormatter;
		FormulaEvaluator evaluator;
	}

	private class Parameters {
		String tableName;
		Mode mode;
	}
}
