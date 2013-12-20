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
import net.cpollet.jixture.tests.mocks.TransactionTemplateMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractTestFixtureLoader {
	@Mock
	private UnitDaoFactory unitDaoFactory;

	@Mock
	private Set<Class> cleanedEntities;

	@Mock
	private Map<Class, TransactionTemplate> transactionTemplates;

	@Mock
	protected UnitDao unitDao;

	private TransactionTemplateMock commitTransactionTemplate;

	private TransactionTemplateMock noCommitTransactionTemplate;


	@Before
	public void setUp() {
		Mockito.when(unitDaoFactory.getUnitDao()).thenReturn(unitDao);

		commitTransactionTemplate = new TransactionTemplateMock();
		noCommitTransactionTemplate = new TransactionTemplateMock();

		Mockito.when(transactionTemplates.get(FixtureLoader.Mode.COMMIT)).thenReturn(commitTransactionTemplate);
		Mockito.when(transactionTemplates.get(FixtureLoader.Mode.NO_COMMIT)).thenReturn(noCommitTransactionTemplate);
	}

	@Test
	public void canLoadReturnsTrueForMappingFixture() {
		// GIVEN

		// WHEN
		boolean canLoad = getFixtureLoader().canLoad(getLoadableFixture());

		// THEN
		assertThat(canLoad).isTrue();
	}

	abstract FixtureLoader getFixtureLoader();

	abstract Fixture getLoadableFixture();

	@Test
	public void canLoadReturnsFalseForNonMappingFixture() {
		// GIVEN

		// WHEN
		boolean canLoad = getFixtureLoader().canLoad(getNonLoadableFixture());

		// THEN
		assertThat(canLoad).isFalse();
	}

	abstract Fixture getNonLoadableFixture();

	@Test
	public void loadInCommitModeUsesCommitTransactionTemplate() {
		// GIVEN

		// WHEN
		getFixtureLoader().load(getLoadableFixture(), FixtureLoader.Mode.COMMIT);

		// THEN
		assertThat(commitTransactionTemplate.isExecuted()).isTrue();
		assertThat(noCommitTransactionTemplate.isExecuted()).isFalse();
	}

	@Test
	public void loadInNoCommitModeUsesNoCommitTransactionTemplate() {
		// GIVEN

		// WHEN
		getFixtureLoader().load(getLoadableFixture(), FixtureLoader.Mode.NO_COMMIT);

		// THEN
		assertThat(commitTransactionTemplate.isExecuted()).isFalse();
		assertThat(noCommitTransactionTemplate.isExecuted()).isTrue();
	}

	@Test
	public void loadFlushesAndClearsHibernateCacheInCommitMode() {
		// GIVEN

		// WHEN
		getFixtureLoader().load(getLoadableFixture(), FixtureLoader.Mode.COMMIT);

		// THEN
		Mockito.verify(unitDao).flushAndClear();
	}

	@Test
	public void loadFlushesAndClearsHibernateCacheInNoCommitMode() {
		// GIVEN

		// WHEN
		getFixtureLoader().load(getLoadableFixture(), FixtureLoader.Mode.NO_COMMIT);

		// THEN
		Mockito.verify(unitDao).flushAndClear();
	}
}
