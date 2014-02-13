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

package net.cpollet.jixture.fixtures;

import net.cpollet.jixture.dao.UnitDao;
import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.tests.mappings.Product;
import net.cpollet.jixture.tests.mappings.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestSqlFileFixture {
	@Mock
	private UnitDaoFactory unitDaoFactory;

	@Mock
	private UnitDao unitDao;

	@Before
	public void setUp() {
		Mockito.when(unitDaoFactory.getUnitDao()).thenReturn(unitDao);
	}

	@Test
	public void getClassesToDeleteReturnsEmptyListOfNotDefined() {
		// GIVEN
		SqlFileFixture fixture = new SqlFileFixture("classpath:tests/fixtures/sql-fixture.sql");

		// WHEN + THEN
		assertThat(fixture.getClassesToDelete()).hasSize(0);
	}

	@Test
	public void getClassesToDeleteReturnsCorrectSetIfDefined() {
		// GIVEN
		SqlFileFixture fixture = new SqlFileFixture("classpath:tests/fixtures/sql-fixture.sql", User.class, Product.class);

		// WHEN + THEN
		assertThat(fixture.getClassesToDelete())//
				.hasSize(2)//
				.contains(User.class, Product.class);
	}

	@Test
	public void loadExecutesQueriesLineByLine() {
		// GIVEN
		SqlFileFixture fixture = new SqlFileFixture("classpath:tests/fixtures/sql-fixture.sql", User.class, Product.class);

		// WHEN
		fixture.load(unitDaoFactory);

		// THEN
		Mockito.verify(unitDao).execute("insert into users values ('username1', 0, 'password'); ");
		Mockito.verify(unitDao).execute("insert into users values ('username2', 0, 'password'); ");
		Mockito.verify(unitDao).execute("insert into users values ('username3', 0, 'password') ; ");
	}
}
