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

package net.cpollet.jixture.fixtures.loaders;

import net.cpollet.jixture.dao.UnitDao;
import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.fixtures.TransformableFixture;
import net.cpollet.jixture.fixtures.transformers.FixtureTransformer;
import net.cpollet.jixture.fixtures.transformers.FixtureTransformerFactory;
import net.cpollet.jixture.tests.mappings.Client;
import net.cpollet.jixture.tests.mappings.User;
import net.cpollet.jixture.tests.mocks.TransactionTemplateMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestSimpleFixtureLoader {
	@Mock
	private UnitDaoFactory unitDaoFactory;

	@Mock
	private Set<Class> cleanedEntities;

	@Mock
	private Map<Class, TransactionTemplate> transactionTemplates;

	@Mock
	protected UnitDao unitDao;

	@Mock
	protected FixtureTransformerFactory fixtureTransformerFactory;

	private TransactionTemplateMock commitTransactionTemplate;

	private TransactionTemplateMock noCommitTransactionTemplate;

	@InjectMocks
	private SimpleFixtureLoader simpleFixtureLoader;

	@Before
	public void setUp() {
		Mockito.when(unitDaoFactory.getUnitDao()).thenReturn(unitDao);

		commitTransactionTemplate = new TransactionTemplateMock();
		noCommitTransactionTemplate = new TransactionTemplateMock();

		Mockito.when(transactionTemplates.get(FixtureLoader.Mode.COMMIT)).thenReturn(commitTransactionTemplate);
		Mockito.when(transactionTemplates.get(FixtureLoader.Mode.NO_COMMIT)).thenReturn(noCommitTransactionTemplate);
	}

	@Test
	public void loadInCommitModeUsesCommitTransactionTemplate() {
		// GIVEN

		// WHEN
		simpleFixtureLoader.load(getLoadableFixture(), FixtureLoader.Mode.COMMIT);

		// THEN
		assertThat(commitTransactionTemplate.isExecuted()).isTrue();
		assertThat(noCommitTransactionTemplate.isExecuted()).isFalse();
	}

	private Fixture getLoadableFixture() {
		return new Fixture() {
			Object object = new Object();

			@Override
			public Fixture addObjects(Object... objects) {
				return this;
			}

			@Override
			public List<Object> getObjects() {
				return Arrays.asList(object);
			}
		};
	}

	@Test
	public void loadInNoCommitModeUsesNoCommitTransactionTemplate() {
		// GIVEN

		// WHEN
		simpleFixtureLoader.load(getLoadableFixture(), FixtureLoader.Mode.NO_COMMIT);

		// THEN
		assertThat(commitTransactionTemplate.isExecuted()).isFalse();
		assertThat(noCommitTransactionTemplate.isExecuted()).isTrue();
	}

	@Test
	public void loadFlushesAndClearsHibernateCacheInCommitMode() {
		// GIVEN

		// WHEN
		simpleFixtureLoader.load(getLoadableFixture(), FixtureLoader.Mode.COMMIT);

		// THEN
		Mockito.verify(unitDao).flushAndClear();
	}

	@Test
	public void loadFlushesAndClearsHibernateCacheInNoCommitMode() {
		// GIVEN

		// WHEN
		simpleFixtureLoader.load(getLoadableFixture(), FixtureLoader.Mode.NO_COMMIT);

		// THEN
		Mockito.verify(unitDao).flushAndClear();
	}

	@Test
	public void loadCleansEntitiesInReverseOrder() {
		// GIVEN

		// WHEN
		simpleFixtureLoader.load(new MappingFixture(//
				new User(),//
				new Client()), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).deleteAll(Client.class);
		inOrder.verify(unitDao).deleteAll(User.class);
	}

	@Test
	public void loadSavesEntitiesInOrder() {
		// GIVEN
		User user = new User();
		Client client = new Client();

		// WHEN
		simpleFixtureLoader.load(new MappingFixture(user, client), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).save(user);
		inOrder.verify(unitDao).save(client);
	}

	@Test
	public void loadDeletesOldEntitiesBeforeSavingNewOnes() {
		// GIVEN
		User user = new User();

		// WHEN
		simpleFixtureLoader.load(new MappingFixture(user), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).deleteAll(User.class);
		inOrder.verify(unitDao).save(user);
	}

	@Test
	public void loadTransformableFixtureTransformsToFixtureBeforeLoading() {
		// GIVEN
		final Fixture expectedFixture = getLoadableFixture();

		FixtureTransformer fixtureTransformer = new FixtureTransformer() {
			@Override
			public Class getFromType() {
				return null;
			}

			@Override
			public Fixture transform(Object fixture) {
				return expectedFixture;
			}
		};

		Mockito.when(fixtureTransformerFactory.getFixtureTransformer(Mockito.any(TransformableFixture.class))).thenReturn(fixtureTransformer);

		// WHEN
		simpleFixtureLoader.load(getLoadableTranfrormableFixture(), FixtureLoader.Mode.COMMIT);

		// THEN
		Mockito.verify(unitDao).save(expectedFixture.getObjects().get(0));
	}

	private TransformableFixture getLoadableTranfrormableFixture() {
		return new TransformableFixture() {
		};
	}

	@Test
	public void resetClearsCleanedEntities() {
		// GIVEN

		// WHEN
		simpleFixtureLoader.reset();

		// THEN
		Mockito.verify(cleanedEntities).clear();
	}
}