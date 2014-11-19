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

package net.cpollet.jixture.support.spring;

import net.cpollet.jixture.fixtures.XmlFileFixture;
import net.cpollet.jixture.fixtures.loaders.FixtureLoader;
import net.cpollet.jixture.support.spring.annotation.Expected;
import net.cpollet.jixture.support.spring.annotation.FixtureDef;
import net.cpollet.jixture.support.spring.annotation.Jixture;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christophe Pollet
 */
@SuppressWarnings({"NoopMethodInAbstractClass", "EmptyClass"})
public abstract class TestClassHolder {
	@Jixture(//
			setup = @FixtureDef(xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})//
	)
	public void annotatedMethod() {
	}

	@Jixture(//
			setup = @FixtureDef(builders = {TestClassForBuilders.MyBuilder.class})//
	)
	public static class TestClassForBuilders {
		public static class MyBuilder implements FixtureBuilder {
			@Override
			public List<net.cpollet.jixture.fixtures.Fixture> build() {
				return Arrays.<net.cpollet.jixture.fixtures.Fixture>asList(new XmlFileFixture("classpath:tests/fixtures/xml-fixture.xml"));
			}
		}
	}

	@Jixture(//
			setup = @FixtureDef(springContextPaths = {"classpath:tests/fixtures/spring-fixture.xml"})//
	)
	public static class TestClassForSpringContextPaths {
	}

	@Jixture(//
			setup = @FixtureDef(xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"})//
	)
	public static class TestClassForXmlFilePaths {
	}

	@Jixture(//
			setup = @FixtureDef(sqlFilePaths = {"classpath:tests/fixtures/sql-fixture.sql"})//
	)
	public static class TestClassForSqlFilePaths {
	}

	@Jixture(//
			setup = @FixtureDef(sqlQueries = {"query"})//
	)
	public static class TestClassForSqlQueries {
	}

	@Jixture(//
			setup = @FixtureDef(xlsFilePaths = {"classpath:tests/fixtures/xls-fixture.xls"})//
	)
	public static class TestClassForXlsFilePaths {
	}

	@Jixture(//
			setup = @FixtureDef(xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})//
	)
	public static class TestClassForXlsxFilePaths {
	}

	@Jixture(//
			setup = @FixtureDef(//
					order = {"xmlFilePaths", "xlsxFilePaths"},//
					xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"},//
					xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})//
	)
	public static class TestClassForXmlAndXlsxFilePaths {
	}

	@Jixture(//
			setup = @FixtureDef(cleaning = {Object.class})//
	)
	public static class TestClassForCleaning {
	}

	@Jixture(//
			setup = {//
					@FixtureDef(mode = FixtureLoader.Mode.COMMIT, xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"}),//
					@FixtureDef(mode = FixtureLoader.Mode.NO_COMMIT, xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})//
			},
			cleanup = {//
					@FixtureDef(mode = FixtureLoader.Mode.COMMIT, xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"}),//
					@FixtureDef(mode = FixtureLoader.Mode.NO_COMMIT, xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})//
			})
	public static class TestClassForDefaultXmlAndXlsxFilePaths {
	}

	@Jixture(//
			setup = @FixtureDef(//
					order = {"xmlFilePaths", "xlsxFilePaths"},//
					xlsFilePaths = {"classpath:tests/fixtures/xls-fixture.xls"},//
					xmlFilePaths = {"classpath:tests/fixtures/xml-fixture.xml"},//
					xlsxFilePaths = {"classpath:tests/fixtures/xlsx-fixture.xlsx"})//
	)
	public static class TestClassForXmlAndXlsxFilePathsButNotXlsFilePath {
	}
}
