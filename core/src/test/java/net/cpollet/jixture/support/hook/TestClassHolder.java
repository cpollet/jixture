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
import net.cpollet.jixture.fixtures.XmlFileFixture;
import net.cpollet.jixture.fixtures.loaders.FixtureLoader;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christophe Pollet
 */
public abstract class TestClassHolder {
	@Jixture(//
			setup = @DataSource(xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})//
	)
	public void annotatedMethod() {
	}

	@SuppressWarnings("EmptyClass")
	@Jixture(//
			setup = @DataSource(builders = {TestClassForBuilders.MyBuilder.class})//
	)
	public static class  TestClassForBuilders {
		public static class  MyBuilder implements FixtureBuilder {
			@Override
			public List<Fixture> build() {
				return Arrays.<Fixture>asList(new XmlFileFixture("classpath:tests/fixtures/xml-fixture.xml"));
			}
		}
	}

	@SuppressWarnings("EmptyClass")
	@Jixture(//
			setup = @DataSource(springContextPaths = {"classpath:tests/fixtures/spring-fixture.xml"})//
	)
	public static class  TestClassForSpringContextPaths {
	}

	@SuppressWarnings("EmptyClass")
	@Jixture(//
			setup = @DataSource(xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"})//
	)
	public static class TestClassForXmlFilePaths {
	}

	@SuppressWarnings("EmptyClass")
	@Jixture(//
			setup = @DataSource(sqlFilePaths = {"classpath:tests/fixtures/sql-fixture.sql"})//
	)
	public static class TestClassForSqlFilePaths {
	}

	@SuppressWarnings("EmptyClass")
	@Jixture(//
			setup = @DataSource(sqlQueries = {"query"})//
	)
	public static class TestClassForSqlQueries {
	}

	@SuppressWarnings("EmptyClass")
	@Jixture(//
			setup = @DataSource(xlsFilePaths = {"classpath:tests/fixtures/xls-fixture.xls"})//
	)
	public static class TestClassForXlsFilePaths {
	}

	@SuppressWarnings("EmptyClass")
	@Jixture(//
			setup = @DataSource(xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})//
	)
	public static class TestClassForXlsxFilePaths {
	}

	@SuppressWarnings("EmptyClass")
	@Jixture(//
			setup = @DataSource(//
					order = {"xmlFilePaths", "xlsxFilePaths"},//
					xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"},//
					xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})//
	)
	public static class TestClassForXmlAndXlsxFilePaths {
	}

	@SuppressWarnings("EmptyClass")
	@Jixture({//
			@DataSource(mode = FixtureLoader.Mode.COMMIT, xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"}),//
			@DataSource(mode = FixtureLoader.Mode.NO_COMMIT, xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})//
	})
	public static class TestClassForDefaultXmlAndXlsxFilePaths {
	}

	@SuppressWarnings("EmptyClass")
	@Jixture(//
			setup = @DataSource(//
					order = {"xmlFilePaths", "xlsxFilePaths"},//
					xlsFilePaths = {"classpath:tests/fixtures/xls-fixture.xls"},//
					xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"},//
					xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})//
	)
	public static class TestClassForXmlAndXlsxFilePathsButNotXlsFilePath {
	}

	@SuppressWarnings("EmptyClass")
	@Jixture(//
			setup = @DataSource(cleaning = {Object.class})//
	)
	public static class TestClassForCleaning {
	}
}
