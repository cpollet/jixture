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

import net.cpollet.jixture.fixtures.BaseFileFixture;
import net.cpollet.jixture.helper.MappingBuilder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Christophe Pollet
 */
public abstract class ExcelFileFixtureTransformer<From extends BaseFileFixture> extends BaseFileFixtureTransformer<From> {
	@Override
	protected List<Object> parse(InputStream inputStream) {
		Workbook workbook = createWorkbook(inputStream);

		DataFormatter dataFormatter = new DataFormatter();
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

		List<Object> mappings = new LinkedList<Object>();

		for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
			Sheet sheet = workbook.getSheetAt(sheetIndex);

			mappings.addAll(parseSheet(sheet, dataFormatter, evaluator));
		}

		return mappings;
	}

	private List<Object> parseSheet(Sheet sheet, DataFormatter dataFormatter, FormulaEvaluator evaluator) {
		String tableName = sheet.getSheetName();

		if (tableName.startsWith("!")) {
			return Collections.emptyList();
		}

		List<Object> mappings = new LinkedList<Object>();

		Iterator<Row> rowIterator = sheet.iterator();

		if (rowIterator.hasNext()) {
			Row columnNames = rowIterator.next();

			while (rowIterator.hasNext()) {
				MappingBuilder mappingBuilder = mappingBuilderFactory.create(tableName);

				Row columnValues = rowIterator.next();

				Iterator<Cell> valuesIterator = columnValues.cellIterator();
				Iterator<Cell> columnsIterator = columnNames.cellIterator();

				while (valuesIterator.hasNext() && columnsIterator.hasNext()) {
					Cell columnNameCell = columnsIterator.next();
					Cell columnValueCell = valuesIterator.next();

					evaluator.evaluateFormulaCell(columnValueCell);

					String columnName = columnNameCell.getStringCellValue();
					String columnValue = dataFormatter.formatCellValue(columnValueCell, evaluator);

					mappingBuilder.withValue(columnName, columnValue);
				}

				mappings.add(mappingBuilder.build());
			}
		}

		return mappings;
	}

	protected abstract Workbook createWorkbook(InputStream inputStream);
}
