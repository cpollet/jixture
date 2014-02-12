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

package net.cpollet.jixture.support;

import net.cpollet.jixture.dao.UnitDao;
import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.loaders.FixtureLoader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author Christophe Pollet
 */
public abstract class AbstractTestSupport implements DatabaseTestSupport, InitializingBean {
	private Collection<Fixture> fixtures;

	@Autowired
	protected UnitDaoFactory unitDaoFactory;

	@Autowired
	protected FixtureLoader fixtureLoader;

	public AbstractTestSupport() {
		fixtures = Collections.emptyList();
	}

	protected abstract FixtureLoader.Mode getCommitMode();

	@Override
	public void beforeTest() {
		for (Fixture fixture : getFixtures()) {
			fixtureLoader.load(fixture, getCommitMode());
		}
	}

	@Override
	public void afterTest() {
		if (fixtureLoader != null) {
			fixtureLoader.reset();
		}
	}

	@Override
	public DatabaseTestSupport setFixtures(Fixture... fixtures) {
		return setFixtures(Arrays.asList(fixtures));
	}

	@Override
	public DatabaseTestSupport setFixtures(Collection<Fixture> fixtures) {
		this.fixtures = fixtures;
		return this;
	}

	@Override
	public DatabaseTestSupport addFixtures(Fixture... fixtures) {
		return addFixtures(Arrays.asList(fixtures));
	}

	@Override
	public DatabaseTestSupport addFixtures(Collection<Fixture> fixtures) {
		this.fixtures.addAll(fixtures);
		return this;
	}

	@Override
	public Collection<Fixture> getFixtures() {
		return fixtures;
	}

	@Override
	public DatabaseTestSupport setFixtureLoader(FixtureLoader fixtureLoader) {
		this.fixtureLoader = fixtureLoader;
		return this;
	}

	@Override
	public UnitDao getUnitDao() {
		return unitDaoFactory.getUnitDao();
	}

	@Override
	public HibernateTransactionManager getTransactionManager() {
		return unitDaoFactory.getTransactionManager();
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(unitDaoFactory, "unitDaoFactory must be set");

		if (!getFixtures().isEmpty()) {
			Assert.notNull(fixtureLoader, "fixturesLoaderChain must be set when getFixtures() does not return an empty Collection");
		}
	}
}
