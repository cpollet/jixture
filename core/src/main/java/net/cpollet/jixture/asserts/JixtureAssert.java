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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Christophe Pollet
 */
public class JixtureAssert<T> {
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

	public JixtureAssert usingTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;

		return this;
	}

	public JixtureAssert containsAtLeast(Fixture fixture) {
		return containsAtLeast(Arrays.asList(fixture));
	}

	public JixtureAssert containsAtLeast(List<Fixture> fixtures) {
		List<Map<String, ?>> expectedMaps = new LinkedList<Map<String, ?>>();

		for (Fixture fixture : fixtures) {
			expectedMaps.addAll(getExpectedMaps(fixture));
		}

		List<Map<String, ?>> actualMaps = getActualMaps();

		if (!actualMaps.containsAll(expectedMaps)) {
			expectedMaps.removeAll(actualMaps);
			throw new AssertionError("Expected but missing elements " + expectedMaps.toString());
		}

		return this;
	}

	private List<Map<String, ?>> getActualMaps() {
		Collection<MappingField> fields = mappingDefinitionHolder.getFieldsByMappingClass(mapping);

		List<Map<String, ?>> actualMaps = new ArrayList<Map<String, ?>>();

		for (Object entity : loadAllEntities()) {
			actualMaps.add(MappingUtils.toMap(entity, fields));
		}

		resetIgnoredColumns(actualMaps);

		return actualMaps;
	}

	private void resetIgnoredColumns(List<Map<String, ?>> maps) {
		if (null == columnsToIgnore) {
			return;
		}

		for (Map<String, ?> map : maps) {
			for (String column : columnsToIgnore) {
				map.remove(column);
			}
		}
	}

	private List<Map<String, ?>> getExpectedMaps(Fixture fixture) {
		Collection<MappingField> fields = mappingDefinitionHolder.getFieldsByMappingClass(mapping);

		ObjectFixture objectFixture = transform(fixture);

		List<Map<String, ?>> expectedMaps = new ArrayList<Map<String, ?>>();

		for (Object object : objectFixture.getObjects()) {
			if (object.getClass().equals(mapping)) {
				expectedMaps.add(MappingUtils.toMap(object, fields));
			}
		}

		resetIgnoredColumns(expectedMaps);

		return expectedMaps;
	}

	@SuppressWarnings("unchecked")
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
		if (null == entities) {
			if (null != transactionTemplate) {
				loadAllEntitiesWithTransactionTemplate();
			}
			else {
				loadAllEntitiesWithoutTransactionTemplate();
			}
		}

		return entities;
	}

	@SuppressWarnings("unchecked")
	private void loadAllEntitiesWithoutTransactionTemplate() {
		entities = getAllEntities();
	}

	private void loadAllEntitiesWithTransactionTemplate() {
		entities = transactionTemplate.execute(new TransactionCallback<List<T>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<T> doInTransaction(TransactionStatus status) {
				return getAllEntities();
			}
		});
	}

	private List getAllEntities() {
		return unitDaoFactory.getUnitDao().getAll(mapping);
	}

	public JixtureAssert containsAtMost(Fixture fixture) {
		return containsAtMost(Arrays.asList(fixture));
	}

	public JixtureAssert containsAtMost(List<Fixture> fixtures) {
		List<Map<String, ?>> expectedMaps = new LinkedList<Map<String, ?>>();

		for (Fixture fixture : fixtures) {
			expectedMaps.addAll(getExpectedMaps(fixture));
		}

		List<Map<String, ?>> actualMaps = getActualMaps();

		actualMaps.removeAll(expectedMaps);

		if (0 != actualMaps.size()) {
			throw new AssertionError("Unexpected but present elements " + actualMaps.toString());
		}

		return this;
	}

	public JixtureAssert containsExactly(Fixture fixture) {
		return containsExactly(Arrays.asList(fixture));
	}

	public JixtureAssert containsExactly(List<Fixture> fixtures) {
		List<String> errors = new ArrayList<String>(2);

		try {
			containsAtLeast(fixtures);
		}
		catch (AssertionError assertionError) {
			errors.add(assertionError.getMessage());
		}

		try {
			containsAtMost(fixtures);
		}
		catch (AssertionError assertionError) {
			errors.add(assertionError.getMessage());
		}

		if (0 < errors.size()) {
			throw new AssertionError(StringUtils.collectionToCommaDelimitedString(errors));
		}

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
