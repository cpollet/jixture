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

package net.cpollet.jixture.hibernate3.helper;


import net.cpollet.jixture.dao.UnitDao;
import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.helper.MappingField;
import net.cpollet.jixture.hibernate3.helper.Hibernate3MappingDefinitionHolder;
import net.cpollet.jixture.tests.mappings.CartEntry;
import net.cpollet.jixture.tests.mappings.Client;
import net.cpollet.jixture.tests.mappings.PersistentObject;
import net.cpollet.jixture.tests.mappings.Product;
import net.cpollet.jixture.tests.mappings.User;
import net.cpollet.jixture.utils.ExceptionUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.fest.assertions.Assertions.assertThat;


/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestHibernate3MappingDefinitionHolder {
	private Hibernate3MappingDefinitionHolder mappingDefinitionHolder;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private UnitDaoFactory unitDaoFactory;

	@Mock
	private UnitDao unitDao;

	@Before
	public void setUp() {
		mappingDefinitionHolder = new Hibernate3MappingDefinitionHolder();

		Mockito.when(unitDaoFactory.getUnitDao()).thenReturn(unitDao);
	}

	private void setProperties() {
		mappingDefinitionHolder.setUnitDaoFactory(unitDaoFactory);

		try {
			mappingDefinitionHolder.afterPropertiesSet();
		}
		catch (Exception e) {
			ExceptionUtils.wrapInRuntimeException(e);
		}
	}

	@Test
	public void getMappingsReturnsMappedClassesFromUnitDao() {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(User.class.getName()));
		setProperties();

		// WHEN
		Collection<Class> mappings = mappingDefinitionHolder.getMappingClasses();

		// THEN
		assertThat(mappings)//
				.hasSize(1)//
				.contains(User.class);
	}

	@Test
	public void getMappingByTableNameReturnsCorrectMappingClass() {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(//
				User.class.getName(),//
				Client.class.getName()));
		setProperties();

		// WHEN
		Class mapping = mappingDefinitionHolder.getMappingClassByTableName("clients");

		// THEN
		assertThat(mapping).isEqualTo(Client.class);
	}

	@Test
	public void getMappingByTableNameThrowsExceptionIfNoMappingFound() {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Collections.<String>emptySet());
		setProperties();

		// THEN
		exception.expect(RuntimeException.class);
		exception.expectMessage("Mapping for table clients not found");

		// WHEN
		mappingDefinitionHolder.getMappingClassByTableName("clients");
	}

	@Test
	public void getFieldByTableAndColumnNamesReturnsCorrectFieldWhenAnnotationOnAttribute() throws NoSuchFieldException {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(//
				User.class.getName(),//
				Client.class.getName()));
		setProperties();

		// WHEN
		MappingField mappingField = mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("users", "username");

		// THEN
		assertThat(mappingField.getField()).isEqualTo(User.class.getDeclaredField("username"));
	}

	@Test
	public void getFieldByTableAndColumnNamesReturnsCorrectFieldWhenAnnotationOnGetter() throws NoSuchFieldException {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(//
				User.class.getName(),//
				Client.class.getName()));
		setProperties();

		// WHEN
		MappingField mappingField = mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("clients", "name");

		// THEN
		assertThat(mappingField.getField()).isEqualTo(Client.class.getDeclaredField("name"));
	}

	@Test
	public void getFieldByTableAndColumnNamesReturnsCorrectFieldWhenNoAnnotation() throws NoSuchFieldException {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(//
				User.class.getName(),//
				Client.class.getName()));
		setProperties();

		// WHEN
		MappingField mappingField = mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("users", "password");

		// THEN
		assertThat(mappingField.getField()).isEqualTo(User.class.getDeclaredField("password"));
	}

	@Test
	public void getFieldByTableAndColumnNameOnNonEmbeddedReturnsANonEmbeddedAttribute() {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(//
				User.class.getName()));
		setProperties();

		// WHEN
		MappingField mappingField = mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("users", "username");

		// THEN
		assertThat(mappingField.isEmbedded()).isFalse();
	}

	@Test
	public void getFieldByTableAndColumnNameOfEmbeddedIdReturnsAnEmbeddedAttribute() {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(//
				CartEntry.class.getName()));
		setProperties();

		// WHEN
		MappingField mappingField = mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("cart_entry", "client_id");

		// THEN
		assertThat(mappingField.isEmbedded()).isTrue();
	}

	@Test
	public void getFieldByTableAndColumnNameOfEmbeddedIdReturnsAnAttributeContainingBothEmbeddableAndField() throws NoSuchFieldException {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(//
				CartEntry.class.getName()));
		setProperties();

		// WHEN
		MappingField mappingField = mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("cart_entry", "client_id");

		// THEN
		assertThat(mappingField.getField()).isEqualTo(CartEntry.class.getDeclaredField("pk"));
		assertThat(mappingField.getEmbeddableField()).isEqualTo(CartEntry.CartEntryPk.class.getDeclaredField("clientId"));
	}

	@Test
	public void getFieldByTableAndColumnNamesThrowsExceptionWhenTableNotFound() {
		// GIVEN
		setProperties();

		// THEN
		exception.expect(RuntimeException.class);
		exception.expectMessage("Mapping class not found for table clients");

		// WHEN
		mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("clients", "name");
	}

	@Test
	public void getFieldByTableAndColumnNamesThrowsExceptionWhenFieldNotFound() {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(//
				User.class.getName(),//
				Client.class.getName()));
		setProperties();

		// THEN
		exception.expect(RuntimeException.class);
		exception.expectMessage("Column birthdate not mapped in mapping class " + Client.class.getName() + " for clients");

		// WHEN
		mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("clients", "birthdate");
	}

	@Test
	public void getFieldsByMappingClassReturnsCorrectListOfFields() throws NoSuchFieldException {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(//
				User.class.getName()));
		setProperties();

		// WHEN
		Collection<MappingField> mappingField = mappingDefinitionHolder.getFieldsByMappingClass(User.class);

		// THEN
		assertThat(mappingField).hasSize(2);
		assertContains(mappingField, new MappingField(User.class.getDeclaredField("username")));
		assertContains(mappingField, new MappingField(User.class.getDeclaredField("password")));
	}

	@Test
	public void getFieldsByMappingClassWithMappedSuperclassReturnsFullListOfFields() throws NoSuchFieldException {
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(//
				Product.class.getName()));
		setProperties();

		// WHEN
		Collection<MappingField> mappingField = mappingDefinitionHolder.getFieldsByMappingClass(Product.class);

		// THEN
		assertThat(mappingField).hasSize(2);
		assertContains(mappingField, new MappingField(Product.class.getDeclaredField("name")));
		assertContains(mappingField, new MappingField(PersistentObject.class.getDeclaredField("id")));
	}

	private void assertContains(Collection<MappingField> mappingFields, MappingField expectedMappingField) {
		boolean found;

		found = false;
		for (MappingField actualMappingField : mappingFields) {
			if (equals(actualMappingField.getField(), expectedMappingField.getField())) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new AssertionError("Expected field " + expectedMappingField.getEmbeddableField() + " / " + expectedMappingField.getField() + " was not found in collection");
		}

		found = false;
		for (MappingField actualMappingField : mappingFields) {
			if (equals(actualMappingField.getEmbeddableField(), expectedMappingField.getEmbeddableField())) {
				found = true;
			}
		}

		if (!found) {
			throw new AssertionError("Expected field " + expectedMappingField.getEmbeddableField() + " / " + expectedMappingField.getField() + " was not found in collection");
		}
	}

	private boolean equals(Field field1, Field field2) {
		if (null == field1 && null == field2) {
			return true;
		}

		return null != field1 && field1.equals(field2);
	}
}
