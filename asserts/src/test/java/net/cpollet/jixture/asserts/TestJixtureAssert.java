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

import net.cpollet.jixture.dao.UnitDao;
import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.fixtures.transformers.FixtureTransformerFactory;
import net.cpollet.jixture.helper.MappingDefinitionHolder;
import net.cpollet.jixture.helper.MappingField;
import net.cpollet.jixture.tests.mappings.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Collections;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestJixtureAssert {
	@Mock
	private MappingDefinitionHolder mappingDefinitionHolder;

	@Mock
	private FixtureTransformerFactory fixtureTransformerFactory;

	@Mock
	private UnitDaoFactory unitDaoFactory;

	@Mock
	private UnitDao unitDao;

	@Before
	public void setUp() throws Exception {
		fixtureTransformerFactory = new FixtureTransformerFactory();
		fixtureTransformerFactory.afterPropertiesSet();

		JixtureAssert.setMappingDefinitionHolder(mappingDefinitionHolder);
		JixtureAssert.setFixtureTransformerFactory(fixtureTransformerFactory);
		JixtureAssert.setUnitDaoFactory(unitDaoFactory);

		Mockito.when(mappingDefinitionHolder.getFieldsByMappingClass(User.class)).thenReturn(Arrays.asList(//
				new MappingField(User.class.getDeclaredField("username")),//
				new MappingField(User.class.getDeclaredField("password"))));

		Mockito.when(unitDaoFactory.getUnitDao()).thenReturn(unitDao);
	}

	@Test
	public void containsAtLeastDoesNotThrowExceptionWhenAssertIsSuccessful() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		User user2 = new User();
		user2.setUsername("username 2");

		Fixture mappingFixture = new MappingFixture(user1);

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1, user2));

		// WHEN
		JixtureAssert.assertThat(User.class).containsAtLeast(mappingFixture);

		// THEN
	}

	@Test
	public void containsAtLeastThrowsExceptionWhenAssertFails() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		User user2 = new User();
		user2.setUsername("username 2");

		Fixture mappingFixture = new MappingFixture(user1, user2);

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN + THEN
		try {
			JixtureAssert.assertThat(User.class).containsAtLeast(mappingFixture);
		}
		catch (AssertionError e) {
			assertThat(e.getMessage()).isEqualTo("Missing several elements [{username=username 2, password=null}]");
			return;
		}

		Assert.fail("AssertionError expected but not thrown");
	}

	@Test
	public void containsAtMostDoesNotThrowExceptionWhenAssertIsSuccessful() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		User user2 = new User();
		user2.setUsername("username 2");

		Fixture mappingFixture = new MappingFixture(user1, user2);

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN
		JixtureAssert.assertThat(User.class).containsAtMost(mappingFixture);

		// THEN
	}

	@Test
	public void containsAtMostThrowsExceptionWhenAssertFails() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		User user2 = new User();
		user2.setUsername("username 2");

		Fixture mappingFixture = new MappingFixture(user1);

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1, user2));

		// WHEN + THEN
		try {
			JixtureAssert.assertThat(User.class).containsAtMost(mappingFixture);
		}
		catch (AssertionError e) {
			assertThat(e.getMessage()).isEqualTo("Too many elements [{username=username 2, password=null}]");
			return;
		}

		Assert.fail("AssertionError expected but not thrown");
	}

	@Test
	public void containsExactlyDoesNotThrowExceptionWhenAssertIsSuccessful() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		User user2 = new User();
		user2.setUsername("username 2");

		Fixture mappingFixture = new MappingFixture(user1, user2);

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1, user2));

		// WHEN
		JixtureAssert.assertThat(User.class).containsExactly(mappingFixture);

		// THEN
	}

	@Test
	public void containsExactlyThrowsExceptionWhenAssertFailsWithNotEnoughExpected() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		User user2 = new User();
		user2.setUsername("username 2");

		Fixture mappingFixture = new MappingFixture(user1);

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1, user2));

		// WHEN + THEN
		try {
			JixtureAssert.assertThat(User.class).containsExactly(mappingFixture);
		}
		catch (AssertionError e) {
			assertThat(e.getMessage()).isEqualTo("Too many elements [{username=username 2, password=null}]");
			return;
		}

		Assert.fail("AssertionError expected but not thrown");
	}

	@Test
	public void containsExactlyThrowsExceptionWhenAssertFailsWithTooManyExpected() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		User user2 = new User();
		user2.setUsername("username 2");

		Fixture mappingFixture = new MappingFixture(user1, user2);

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN + THEN
		try {
			JixtureAssert.assertThat(User.class).containsExactly(mappingFixture);
		}
		catch (AssertionError e) {
			assertThat(e.getMessage()).isEqualTo("Missing several elements [{username=username 2, password=null}]");
			return;
		}

		Assert.fail("AssertionError expected but not thrown");
	}

	@Test
	public void rowsCountIsAtLeastDoesNotThrowExceptionWhenAssertIsSuccessful() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN
		JixtureAssert.assertThat(User.class).rowsCountIsAtLeast(1);

		// THEN
	}

	@Test
	public void rowsCountIsAtLeastThrowsExceptionWhenAssertFails() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN + THEN
		try {
			JixtureAssert.assertThat(User.class).rowsCountIsAtLeast(2);
		}
		catch (AssertionError e) {
			assertThat(e.getMessage()).isEqualTo("Expected at least 2 rows but got only 1");

			return;
		}

		Assert.fail("AssertionError expected but not thrown");
	}

	@Test
	public void rowsCountIsAtMostDoesNotThrowExceptionWhenAssertIsSuccessful() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN
		JixtureAssert.assertThat(User.class).rowsCountIsAtMost(1);

		// THEN
	}

	@Test
	public void rowsCountIsAtMostThrowsExceptionWhenAssertFails() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN + THEN
		try {
			JixtureAssert.assertThat(User.class).rowsCountIsAtMost(0);
		}
		catch (AssertionError e) {
			assertThat(e.getMessage()).isEqualTo("Expected at most 0 rows but got 1");

			return;
		}

		Assert.fail("AssertionError expected but not thrown");
	}

	@Test
	public void rowsCountIsExactlyDoesNotThrowExceptionWhenAssertIsSuccessful() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN
		JixtureAssert.assertThat(User.class).rowsCountIsExactly(1);

		// THEN
	}

	@Test
	public void rowsCountIsExactlyThrowsExceptionWhenNotEnoughRows() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN + THEN
		try {
			JixtureAssert.assertThat(User.class).rowsCountIsExactly(2);
		}
		catch (AssertionError e) {
			assertThat(e.getMessage()).isEqualTo("Expected at least 2 rows but got only 1");

			return;
		}

		Assert.fail("AssertionError expected but not thrown");
	}

	@Test
	public void rowsCountIsExactlyThrowsExceptionWhenToManyRows() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN + THEN
		try {
			JixtureAssert.assertThat(User.class).rowsCountIsExactly(0);
		}
		catch (AssertionError e) {
			assertThat(e.getMessage()).isEqualTo("Expected at most 0 rows but got 1");

			return;
		}

		Assert.fail("AssertionError expected but not thrown");
	}

	@Test
	public void isEmptyDoesNotThrowExceptionWhenAssertIsSuccessful() {
		// GIVEN
		Mockito.when(unitDao.getAll(User.class)).thenReturn(Collections.<User>emptyList());

		// WHEN
		JixtureAssert.assertThat(User.class).isEmpty();

		// THEN
	}

	@Test
	public void isEmptyThrowsExceptionWhenAssertFails() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN + THEN
		try {
			JixtureAssert.assertThat(User.class).isEmpty();
		}
		catch (AssertionError e) {
			assertThat(e.getMessage()).isEqualTo("Expected at most 0 rows but got 1");
			return;
		}

		Assert.fail("AssertionError expected but not thrown");
	}

	@Test
	public void isNotEmptyDoesNotThrowExceptionWhenAssertIsSuccessful() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username 1");

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		// WHEN
		JixtureAssert.assertThat(User.class).isNotEmpty();

		// THEN
	}

	@Test
	public void isNotEmptyThrowsExceptionWhenAssertFails() {
		// GIVEN
		Mockito.when(unitDao.getAll(User.class)).thenReturn(Collections.<User>emptyList());

		// WHEN + THEN
		try {
			JixtureAssert.assertThat(User.class).isNotEmpty();
		}
		catch (AssertionError e) {
			assertThat(e.getMessage()).isEqualTo("Expected at least 1 rows but got only 0");
			return;
		}

		Assert.fail("AssertionError expected but not thrown");
	}

	@Test
	public void containsExactlyFailsWhenNonIgnoredColumnsAreDifferent() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username");
		user1.setPassword("password 1");

		User user2 = new User();
		user2.setUsername("username");
		user2.setPassword("password 2");

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		Fixture mappingFixture = new MappingFixture(user2);

		// WHEN + THEN
		try {
			JixtureAssert.assertThat(User.class).containsExactly(mappingFixture);
		}
		catch (AssertionError e) {
			assertThat(e.getMessage()).isEqualTo("Missing several elements [{username=username, password=password 2}]");
			return;
		}
	}

	@Test
	public void withoutConsideringColumnsDoesNotCheckIgnoredColumns() {
		// GIVEN
		User user1 = new User();
		user1.setUsername("username");
		user1.setPassword("password 1");

		User user2 = new User();
		user2.setUsername("username");
		user2.setPassword("password 2");

		Mockito.when(unitDao.getAll(User.class)).thenReturn(Arrays.asList(user1));

		Fixture mappingFixture = new MappingFixture(user2);

		// WHEN
		JixtureAssert.assertThat(User.class).withoutConsideringColumns("password").containsExactly(mappingFixture);

		// THEN
	}

	@Test
	public void transactionTemplateIsUsedWhenSet() {
		// GIVEN
		Mockito.when(unitDao.getAll(User.class)).thenReturn(Collections.<User>emptyList());

		final boolean[] transactionTemplatedUsed = {false};
		TransactionTemplate transactionTemplate = new TransactionTemplate() {
			@Override
			public <T> T execute(TransactionCallback<T> action) throws TransactionException {
				transactionTemplatedUsed[0] = true;
				return action.doInTransaction(null);
			}
		};

		// WHEN
		JixtureAssert.assertThat(User.class).usingTransactionTemplate(transactionTemplate).isEmpty();

		// THEN
		assertThat(transactionTemplatedUsed[0]).isTrue();
	}
}
