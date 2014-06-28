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

package net.cpollet.jixture.sample.test.support;

import net.cpollet.jixture.dao.UnitDao;
import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.loaders.FixtureLoader;
import net.cpollet.jixture.support.DatabaseTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

import java.util.Collection;

/**
 * @author Christophe Pollet
 */
public abstract class AbstractSetupEnvironmentDatabaseTest implements DatabaseTestSupport {
	@BeforeClass
	public static void setUpEnvironment() {
		System.setProperty("env.type", "integration");
	}

	@Override
	@Before
	public void beforeTest() {
		setupFixtures();
		getDatabaseTestSupport().beforeTest();
	}

	@Override
	@After
	public void afterTest() {
		teardownFixtures();
		getDatabaseTestSupport().afterTest();
	}

	protected abstract DatabaseTestSupport getDatabaseTestSupport();

	@Override
	public void setupFixtures() {
		getDatabaseTestSupport().setupFixtures();
	}

	@Override
	public void teardownFixtures() {
		getDatabaseTestSupport().teardownFixtures();
	}

	@Override
	public DatabaseTestSupport addFixtures(Fixture... fixtures) {
		return getDatabaseTestSupport().addFixtures(fixtures);
	}

	@Override
	public DatabaseTestSupport addFixtures(Collection<Fixture> fixtures) {
		return getDatabaseTestSupport().addFixtures(fixtures);
	}

	@Override
	public DatabaseTestSupport addFixtures(String... contexts) {
		return getDatabaseTestSupport().addFixtures(contexts);
	}

	@Override
	public Collection<Fixture> getFixtures() {
		return getDatabaseTestSupport().getFixtures();
	}

	@Override
	public DatabaseTestSupport setFixtureLoader(FixtureLoader fixtureLoaderChain) {
		return getDatabaseTestSupport().setFixtureLoader(fixtureLoaderChain);
	}

	@Override
	public UnitDao getUnitDao() {
		return getDatabaseTestSupport().getUnitDao();
	}

	@Override
	public HibernateTransactionManager getTransactionManager() {
		return getDatabaseTestSupport().getTransactionManager();
	}
}
