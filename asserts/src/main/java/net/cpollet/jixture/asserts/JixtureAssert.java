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

package net.cpollet.jixture.asserts;

import net.cpollet.jixture.asserts.helpers.MappingUtils;
import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.ObjectFixture;
import net.cpollet.jixture.fixtures.TransformableFixture;
import net.cpollet.jixture.fixtures.transformers.FixtureTransformerFactory;
import net.cpollet.jixture.helper.MappingDefinitionHolder;
import net.cpollet.jixture.helper.MappingField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Christophe Pollet
 */
public class JixtureAssert<T> {
	private static final Logger logger = LoggerFactory.getLogger(JixtureAssert.class);

	private Class mapping;
	private String[] columnsToIgnore;
	private TransactionTemplate transactionTemplate;

	private List<T> entities;

	private static UnitDaoFactory unitDaoFactory;

	private static FixtureTransformerFactory fixtureTransformerFactory;

	private static MappingDefinitionHolder mappingDefinitionHolder;

	public static void setUnitDaoFactory(UnitDaoFactory unitDaoFactory) {
		JixtureAssert.unitDaoFactory = unitDaoFactory;
	}

	public static void setFixtureTransformerFactory(FixtureTransformerFactory fixtureTransformerFactory) {
		JixtureAssert.fixtureTransformerFactory = fixtureTransformerFactory;
	}

	public static void setMappingDefinitionHolder(MappingDefinitionHolder mappingDefinitionHolder) {
		JixtureAssert.mappingDefinitionHolder = mappingDefinitionHolder;
	}

	private JixtureAssert(Class mapping) {
		this.mapping = mapping;
	}

	public static JixtureAssert assertThat(Class mapping) {
		return new JixtureAssert(mapping);
	}

	public JixtureAssert withoutConsideringColumns(String... columns) {
		this.columnsToIgnore = columns;

		return this;
	}

	public JixtureAssert usingTransactionTemplate(TransactionTemplate transactonTemplate) {
		this.transactionTemplate = transactonTemplate;

		return this;
	}

	public JixtureAssert containsAtLeast(Fixture transformableFixture) {
		List<Map<String, ? extends Object>> expectedMaps = getExpectedMaps(transformableFixture);
		List<Map<String, ? extends Object>> actualMaps = getActualMaps();

		if (!actualMaps.containsAll(expectedMaps)) {
			expectedMaps.removeAll(actualMaps);
			throw new AssertionError("Missing several elements " + expectedMaps.toString());
		}

		return this;
	}

	private List<Map<String, ? extends Object>> getActualMaps() {
		Collection<MappingField> fields = mappingDefinitionHolder.getFieldsByMappingClass(mapping);

		List<Map<String, ? extends Object>> actualMaps = new ArrayList<Map<String, ? extends Object>>();

		for (Object entity : loadAllEntities()) {
			actualMaps.add(MappingUtils.toMap(entity, fields));
		}

		resetIgnoredColumns(actualMaps);

		return actualMaps;
	}

	private void resetIgnoredColumns(List<Map<String, ? extends Object>> maps) {
		if (columnsToIgnore == null) {
			return;
		}

		for (Map<String, ? extends Object> map : maps) {
			for (String column : columnsToIgnore) {
				map.remove(column);
			}
		}
	}

	private List<Map<String, ? extends Object>> getExpectedMaps(Fixture transformableFixture) {
		Collection<MappingField> fields = mappingDefinitionHolder.getFieldsByMappingClass(mapping);

		ObjectFixture fixture = transform(transformableFixture);

		List<Map<String, ? extends Object>> expectedMaps = new ArrayList<Map<String, ? extends Object>>();

		for (Object object : fixture.getObjects()) {
			if (object.getClass().equals(mapping)) {
				expectedMaps.add(MappingUtils.toMap(object, fields));
			}
		}

		resetIgnoredColumns(expectedMaps);

		return expectedMaps;
	}

	private ObjectFixture transform(Fixture fixture) {
		while (fixture instanceof TransformableFixture) {
			fixture = fixtureTransformerFactory.getFixtureTransformer(fixture).transform(fixture);
		}

		if (fixture instanceof ObjectFixture) {
			return (ObjectFixture) fixture;
		}

		throw new IllegalArgumentException("Fixture must be transformable into an " + ObjectFixture.class.getName());
	}

	private List<T> loadAllEntities() {
		if (entities == null) {
			if (transactionTemplate != null) {
				loadAllEntitiesWithTransactionTemplate();
			}
			else {
				loadAllEntitiesWithoutTransactionTemplate();
			}
		}

		return entities;
	}

	private void loadAllEntitiesWithoutTransactionTemplate() {
		entities = getAllEntities();
	}

	private void loadAllEntitiesWithTransactionTemplate() {
		entities = transactionTemplate.execute(new TransactionCallback<List<T>>() {
			@Override
			public List<T> doInTransaction(TransactionStatus status) {
				return getAllEntities();
			}
		});
	}

	private List getAllEntities() {
		return unitDaoFactory.getUnitDao().getAll(mapping);
	}

	public JixtureAssert containsAtMost(Fixture transformableFixture) {
		List<Map<String, ? extends Object>> expectedMaps = getExpectedMaps(transformableFixture);
		List<Map<String, ? extends Object>> actualMaps = getActualMaps();

		actualMaps.removeAll(expectedMaps);

		if (actualMaps.size() > 0) {
			throw new AssertionError("Too many elements " + actualMaps.toString());
		}

		return this;
	}

	public JixtureAssert containsExactly(Fixture fixture) {
		containsAtLeast(fixture);
		containsAtMost(fixture);
		return this;
	}

	public JixtureAssert rowsCountIsAtLeast(Integer rowsCount) {
		if (loadAllEntities().size() < rowsCount) {
			throw new AssertionError("Expected at least " + rowsCount + " rows but got only " + loadAllEntities().size());
		}

		return this;
	}

	public JixtureAssert rowsCountIsAtMost(Integer rowsCount) {
		if (loadAllEntities().size() > rowsCount) {
			throw new AssertionError("Expected at most " + rowsCount + " rows but got " + loadAllEntities().size());
		}

		return this;
	}

	public JixtureAssert rowsCountIsExactly(Integer rowsCount) {
		rowsCountIsAtLeast(rowsCount);
		rowsCountIsAtMost(rowsCount);
		return this;
	}

	public JixtureAssert isEmpty() {
		rowsCountIsExactly(0);
		return this;
	}

	public JixtureAssert isNotEmpty() {
		rowsCountIsAtLeast(1);
		return this;
	}
}
