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

import net.cpollet.jixture.fixtures.CleaningFixture;
import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.SpringFixture;
import net.cpollet.jixture.fixtures.SqlFileFixture;
import net.cpollet.jixture.fixtures.SqlFixture;
import net.cpollet.jixture.fixtures.XlsFileFixture;
import net.cpollet.jixture.fixtures.XlsxFileFixture;
import net.cpollet.jixture.fixtures.XmlFileFixture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
* @author Christophe Pollet
*/
enum FixtureType {
	BUILDER("builders", new FixtureBuilderBuilder() {
		@SuppressWarnings("unchecked")
		@Override
		public List<Fixture> build() {
			Class<FixtureBuilder>[] classes = (Class<FixtureBuilder>[]) object;

			List<Fixture> fixtures = new LinkedList<Fixture>();

			for (Class<FixtureBuilder> clazz : classes) {
				FixtureBuilder fixtureBuilder = instantiate(clazz);
				fixtures.addAll(fixtureBuilder.build());
			}

			return fixtures;
		}

		private FixtureBuilder instantiate(Class<FixtureBuilder> clazz) {
			try {
				return clazz.newInstance();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}),//
	SPRING_CONTEXT("springContextPaths", new FixtureBuilderBuilder() {
		@Override
		public List<Fixture> build() {
			String[] paths = (String[]) object;
			List<Fixture> fixtures = new ArrayList<Fixture>(paths.length);

			for (String path : paths) {
				fixtures.add(new SpringFixture(path, Object.class));
			}

			return fixtures;
		}
	}),//
	SQL_FILE("sqlFilePaths", new FixtureBuilderBuilder() {
		@Override
		public List<Fixture> build() {
			String[] paths = (String[]) object;
			List<Fixture> fixtures = new ArrayList<Fixture>(paths.length);

			for (String path : paths) {
				fixtures.add(new SqlFileFixture(path));
			}

			return fixtures;
		}
	}),//
	SQL_QUERIES("sqlQueries", new FixtureBuilderBuilder() {
		@Override
		public List<Fixture> build() {
			String[] queries = (String[]) object;
			List<Fixture> fixtures = new ArrayList<Fixture>(queries.length);

			fixtures.add(new SqlFixture(queries));

			return fixtures;
		}
	}),//
	XLS_FILE("xlsFilePaths", new FixtureBuilderBuilder() {
		@Override
		public List<Fixture> build() {
			String[] paths = (String[]) object;
			List<Fixture> fixtures = new ArrayList<Fixture>(paths.length);

			for (String path : paths) {
				fixtures.add(new XlsFileFixture(path));
			}

			return fixtures;
		}
	}),//
	XLSX_FILE("xlsxFilePaths", new FixtureBuilderBuilder() {
		@Override
		public List<Fixture> build() {
			String[] paths = (String[]) object;
			List<Fixture> fixtures = new ArrayList<Fixture>(paths.length);

			for (String path : paths) {
				fixtures.add(new XlsxFileFixture(path));
			}

			return fixtures;
		}
	}),//
	XML_FILE("xmlFilePaths", new FixtureBuilderBuilder() {
		@Override
		public List<Fixture> build() {
			String[] paths = (String[]) object;
			List<Fixture> fixtures = new ArrayList<Fixture>(paths.length);

			for (String path : paths) {
				fixtures.add(new XmlFileFixture(path));
			}

			return fixtures;
		}
	}),//
	CLEANING("cleaning", new FixtureBuilderBuilder() {
		@Override
		public List<? extends Fixture> build() {
			Class[] classes = (Class[]) object;
			return Arrays.asList(new CleaningFixture(classes));
		}
	});

	private String name;
	private FixtureBuilderBuilder fixtureBuilderBuilder;

	FixtureType(String name, FixtureBuilderBuilder fixtureBuilderBuilder) {
		this.name = name;
		this.fixtureBuilderBuilder = fixtureBuilderBuilder;
	}

	public FixtureBuilderBuilder getFixtureBuilderBuilder() {
		return fixtureBuilderBuilder;
	}

	public static FixtureType getByName(String name) {
		for (FixtureType fixtureType : values()) {
			if (fixtureType.name.equals(name)) {
				return fixtureType;
			}
		}

		throw new IllegalArgumentException("No FixtureType found for name '" + name + "'");
	}
}
