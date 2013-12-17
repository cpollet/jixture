package net.cpollet.jixture.helper;


import net.cpollet.jixture.dao.UnitDao;
import net.cpollet.jixture.dao.UnitDaoFactory;

import net.cpollet.jixture.tests.mappings.Client;
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
public class TestMappingDefinitionHolderImpl {
	private MappingDefinitionHolderImpl mappingDefinitionHolder;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private UnitDaoFactory unitDaoFactory;

	@Mock
	private UnitDao unitDao;

	@Before
	public void setUp() {
		mappingDefinitionHolder = new MappingDefinitionHolderImpl();

		Mockito.when(unitDaoFactory.getUnitDao()).thenReturn(unitDao);
	}

	private void setProperties() {
		mappingDefinitionHolder.setUnitDaoFactory(unitDaoFactory);

		try {
			mappingDefinitionHolder.afterPropertiesSet();
		} catch (Exception e) {
			ExceptionUtils.wrapInRuntimeException(e);
		}
	}

	@Test
	public void getMappingsReturnsMappedClassesFromUnitDao() {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(User.class.getName()));
		setProperties();

		// WHEN
		Collection<Class> mappings = mappingDefinitionHolder.getMappings();

		// THEN
		assertThat(mappings)
				.hasSize(1)
				.contains(User.class);
	}

	@Test
	public void getMappingByTableNameReturnsCorrectMappingClass() {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(
				User.class.getName(),
				Client.class.getName()
		));
		setProperties();

		// WHEN
		Class mapping = mappingDefinitionHolder.getMappingByTableName("clients");

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
		mappingDefinitionHolder.getMappingByTableName("clients");
	}

	@Test
	public void getFieldByTableAndColumnNamesReturnsCorrectFieldWhenAnnotationOnAttribute() throws NoSuchFieldException {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(
				User.class.getName(),
				Client.class.getName()
		));
		setProperties();

		// WHEN
		Field field = mappingDefinitionHolder.getFieldByTableAndColumnNames("users", "username");

		// THEN
		assertThat(field).isEqualTo(User.class.getDeclaredField("username"));
	}

	@Test
	public void getFieldByTableAndColumnNamesReturnsCorrectFieldWhenAnnotationOnGetter() throws NoSuchFieldException {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(
				User.class.getName(),
				Client.class.getName()
		));
		setProperties();

		// WHEN
		Field field = mappingDefinitionHolder.getFieldByTableAndColumnNames("clients", "name");

		// THEN
		assertThat(field).isEqualTo(Client.class.getDeclaredField("name"));
	}

	@Test
	public void getFieldByTableAndColumnNamesReturnsCorrectFieldWhenNoAnnotation() throws NoSuchFieldException {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(
				User.class.getName(),
				Client.class.getName()
		));
		setProperties();

		// WHEN
		Field field = mappingDefinitionHolder.getFieldByTableAndColumnNames("users", "password");

		// THEN
		assertThat(field).isEqualTo(User.class.getDeclaredField("password"));
	}

	@Test
	public void getFieldByTableAndColumnNamesThrowsExceptionWhenTableNotFound() {
		// GIVEN
		setProperties();

		// THEN
		exception.expect(RuntimeException.class);
		exception.expectMessage("Mapping for table clients not found");

		// WHEN
		mappingDefinitionHolder.getFieldByTableAndColumnNames("clients", "name");
	}

	@Test
	public void getFieldByTableAndColumnNamesThrowsExceptionWhenFieldNotFound() {
		// GIVEN
		Mockito.when(unitDao.getKnownMappings()).thenReturn(Sets.newSet(
				User.class.getName(),
				Client.class.getName()
		));
		setProperties();

		// THEN
		exception.expect(RuntimeException.class);
		exception.expectMessage("Column birthdate not mapped in mapping class " + Client.class.getName() + " for clients");

		// WHEN
		mappingDefinitionHolder.getFieldByTableAndColumnNames("clients", "birthdate");
	}
}
