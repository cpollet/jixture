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

package net.cpollet.jixture.support.hook;

import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.SpringFixture;
import net.cpollet.jixture.fixtures.SqlFileFixture;
import net.cpollet.jixture.fixtures.SqlFixture;
import net.cpollet.jixture.fixtures.XlsFileFixture;
import net.cpollet.jixture.fixtures.XlsxFileFixture;
import net.cpollet.jixture.fixtures.XmlFileFixture;
import net.cpollet.jixture.fixtures.loaders.FixtureLoader;
import net.cpollet.jixture.support.DatabaseTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.JixtureTestContext;
import org.springframework.test.context.TestContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestJixtureTestExecutionListener {
	private JixtureTestExecutionListener listener;

	@Mock
	private ApplicationContext applicationContext;

	@Mock
	private DatabaseTestSupport noCommitDatabaseTestSupport;

	@Before
	public void setUp() {
		listener = new JixtureTestExecutionListener();

		Map<FixtureLoader.Mode, DatabaseTestSupport> databaseTestSupportMap = new HashMap<FixtureLoader.Mode, DatabaseTestSupport>();
		databaseTestSupportMap.put(FixtureLoader.Mode.NO_COMMIT, noCommitDatabaseTestSupport);

		Mockito.when(applicationContext.getBean("jixture.databaseTestSupportByMode")).thenReturn(databaseTestSupportMap);
	}

	@Test
	public void beforeTestMethodLoadsBuilderFixtures() throws Exception {
		List<Fixture> fixtures = load(TestClassForBuilders.class);
		assertThat(fixtures.size()).isEqualTo(1);
		assertThat(fixtures.get(0)).isInstanceOf(XmlFileFixture.class);
	}

	@Test
	public void beforeTestMethodLoadsSpringContextFixtures() throws Exception {
		List<Fixture> fixtures = load(TestClassForSpringContextPaths.class);
		assertThat(fixtures.size()).isEqualTo(1);
		assertThat(fixtures.get(0)).isInstanceOf(SpringFixture.class);
	}

	@Test
	public void beforeTestMethodLoadsXmlFileFixtures() throws Exception {
		List<Fixture> fixtures = load(TestClassForXmlFilePaths.class);
		assertThat(fixtures.size()).isEqualTo(1);
		assertThat(fixtures.get(0)).isInstanceOf(XmlFileFixture.class);
	}

	@Test
	public void beforeTestMethodLoadsSqlFileFixtures() throws Exception {
		List<Fixture> fixtures = load(TestClassForSqlFilePaths.class);
		assertThat(fixtures.size()).isEqualTo(1);
		assertThat(fixtures.get(0)).isNotNull();
		assertThat(fixtures.get(0)).isInstanceOf(SqlFileFixture.class);
	}

	@Test
	public void beforeTestMethodLoadsSqlQueriesFixtures() throws Exception {
		List<Fixture> fixtures = load(TestClassForSqlQueries.class);
		assertThat(fixtures.size()).isEqualTo(1);
		assertThat(fixtures.get(0)).isNotNull();
		assertThat(fixtures.get(0)).isInstanceOf(SqlFixture.class);
	}

	@Test
	public void beforeTestMethodLoadsXlsFileFixtures() throws Exception {
		List<Fixture> fixtures = load(TestClassForXlsFilePaths.class);
		assertThat(fixtures.size()).isEqualTo(1);
		assertThat(fixtures.get(0)).isNotNull();
		assertThat(fixtures.get(0)).isInstanceOf(XlsFileFixture.class);
	}

	@Test
	public void beforeTestMethodLoadsXlsxFileFixtures() throws Exception {
		List<Fixture> fixtures = load(TestClassForXlsxFilePaths.class);
		assertThat(fixtures.size()).isEqualTo(1);
		assertThat(fixtures.get(0)).isNotNull();
		assertThat(fixtures.get(0)).isInstanceOf(XlsxFileFixture.class);
	}

	@Test
	public void beforeTestMethodLoadsFixturesInOrder() throws Exception {
		List<Fixture> fixtures = load(TestClassForXmlAndXlsxFilePaths.class, 2);
		assertThat(fixtures.size()).isEqualTo(2);
		assertThat(fixtures.get(0)).isNotNull();
		assertThat(fixtures.get(0)).isInstanceOf(XmlFileFixture.class);
		assertThat(fixtures.get(1)).isNotNull();
		assertThat(fixtures.get(1)).isInstanceOf(XlsxFileFixture.class);
	}

	@Test
	public void beforeTestMethodDoesNotLoadFixturesNotSpecified() throws Exception {
		List<Fixture> fixtures = load(TestClassForXmlAndXlsxFilePathsButNotXlsFilePath.class, 2);
		assertThat(fixtures.size()).isEqualTo(2);
		assertThat(fixtures.get(0)).isNotNull();
		assertThat(fixtures.get(0)).isInstanceOf(XmlFileFixture.class);
		assertThat(fixtures.get(1)).isNotNull();
		assertThat(fixtures.get(1)).isInstanceOf(XlsxFileFixture.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void beforeTestMethodLoadsFixturesOnMethods() throws Exception {
		// GIVEN
		Method method = getClass().getMethod("annotatedMethod");
		TestContext testContext = new JixtureTestContext(getClass(), method, applicationContext);

		// WHEN
		listener.beforeTestMethod(testContext);

		// THEN
		ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
		Mockito.verify(noCommitDatabaseTestSupport, Mockito.times(1)).addFixtures(argumentCaptor.capture());
		Mockito.verify(noCommitDatabaseTestSupport).loadFixtures();

		List<Fixture> fixtures = flatten(argumentCaptor.getAllValues());

		assertThat(fixtures.size()).isEqualTo(1);
		assertThat(fixtures.get(0)).isNotNull();
		assertThat(fixtures.get(0)).isInstanceOf(XlsxFileFixture.class);
	}

	private List<Fixture> load(Class clazz) throws Exception {
		return load(clazz, 1);
	}

	@SuppressWarnings("unchecked")
	private List<Fixture> load(Class clazz, int fixturesCount) throws Exception {
		// GIVEN
		TestContext testContext = new JixtureTestContext(clazz, applicationContext);

		// WHEN
		listener.beforeTestMethod(testContext);

		// THEN
		ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
		Mockito.verify(noCommitDatabaseTestSupport, Mockito.times(fixturesCount)).addFixtures(argumentCaptor.capture());
		Mockito.verify(noCommitDatabaseTestSupport).loadFixtures();

		return flatten(argumentCaptor.getAllValues());
	}

	@SuppressWarnings("unchecked")
	private List flatten(List<List> listOfLists) {
		List flattenedList = new LinkedList();

		for (List innerList : listOfLists) {
			flattenedList.addAll(innerList);
		}

		return flattenedList;
	}

	@PrepareData(xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})
	public void annotatedMethod() {
	}

	@SuppressWarnings("EmptyClass")
	@PrepareData(builders = {TestClassForBuilders.MyBuilder.class})
	public static class TestClassForBuilders {
		public static class MyBuilder implements FixtureBuilder {
			@Override
			public List<Fixture> build() {
				return Arrays.<Fixture>asList(new XmlFileFixture("classpath:tests/fixtures/xml-fixture.xml"));
			}
		}
	}

	@SuppressWarnings("EmptyClass")
	@PrepareData(springContextPaths = {"classpath:tests/fixtures/spring-fixture.xml"})
	private class TestClassForSpringContextPaths {
	}

	@SuppressWarnings("EmptyClass")
	@PrepareData(xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"})
	private class TestClassForXmlFilePaths {
	}

	@SuppressWarnings("EmptyClass")
	@PrepareData(sqlFilePaths = {"classpath:tests/fixtures/sql-fixture.sql"})
	private class TestClassForSqlFilePaths {
	}

	@SuppressWarnings("EmptyClass")
	@PrepareData(sqlQueries = {"query"})
	private class TestClassForSqlQueries {
	}

	@SuppressWarnings("EmptyClass")
	@PrepareData(xlsFilePaths = {"classpath:tests/fixtures/xls-fixture.xls"})
	private class TestClassForXlsFilePaths {
	}

	@SuppressWarnings("EmptyClass")
	@PrepareData(xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})
	private class TestClassForXlsxFilePaths {
	}

	@SuppressWarnings("EmptyClass")
	@PrepareData(//
			order = {"xmlFilePaths", "xlsxFilePaths"},//
			xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"},//
			xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})
	private class TestClassForXmlAndXlsxFilePaths {
	}

	@SuppressWarnings("EmptyClass")
	@PrepareData(//
			order = {"xmlFilePaths", "xlsxFilePaths"},//
			xlsFilePaths = {"classpath:tests/fixtures/xls-fixture.xls"},//
			xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"},//
			xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})
	private class TestClassForXmlAndXlsxFilePathsButNotXlsFilePath {
	}
}
