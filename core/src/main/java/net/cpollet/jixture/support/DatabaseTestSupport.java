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
import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.loaders.FixtureLoader;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

import java.util.Collection;

/**
 * Provides a set of method that can be useful in the context of unit/integration testing. This interface
 * has two kinds of implementations. One kind is abstract classes that are meant to be superclasses for test
 * classes ({@link net.cpollet.jixture.support.AbstractDatabaseTest} and subclasses), the other kind is support classes
 * that can be injected in a test class.
 *
 * @see net.cpollet.jixture.support.AbstractDatabaseTest
 * @see net.cpollet.jixture.support.AbstractCommitDatabaseTest
 * @see net.cpollet.jixture.support.AbstractNoCommitDatabaseTest
 * @see net.cpollet.jixture.support.CommitDatabaseTestSupport
 * @see net.cpollet.jixture.support.NoCommitDatabaseTestSupport
 *
 * @author Christophe Pollet
 */
public interface DatabaseTestSupport {
	/**
	 * Prepares the fixtures to be loaded. This method is meant to be executed in the {@code @Before}-annotated method.
	 */
	void setupFixtures();

	/**
	 * Resets the fixtures after test methods. This method is meant to be executed in the {@code @After}-annotated
	 * method.
	 */
	void teardownFixtures();

	/**
	 * Adds fixtures to the fixtures to be loaded.
	 *
	 * @param fixtures the fixtures to add.
	 *
	 * @return the current instance.
	 */
	DatabaseTestSupport addFixtures(Fixture... fixtures);

	/**
	 * Adds a collection fixtures to the fixtures to be loaded.
	 *
	 * @param fixtures the fixtures to add.
	 *
	 * @return the current instance.
	 */
	DatabaseTestSupport addFixtures(Collection<Fixture> fixtures);

	/**
	 * Adds fixtures to the fixtures to be loaded. This methods expects paths to a Spring XML context containing
	 * fixtures beans.
	 *
	 * @param contexts the fixtures to add.
	 *
	 * @return the current instance.
	 */
	DatabaseTestSupport addFixtures(String... contexts);

	/**
	 * Returns the registered fixtures.
	 *
	 * @return the registered fixtures.
	 */
	Collection<Fixture> getFixtures();

	/**
	 * Sets the fixture loader.
	 *
	 * @param fixtureLoaderChain the fixture loader
	 *
	 * @return the current instance.
	 */
	DatabaseTestSupport setFixtureLoader(FixtureLoader fixtureLoaderChain);

	/**
	 * This method is meant to be executed after a test in run. Typically loads the fixtures. Depending on the context,
	 * it can be annotated with {@code @After}.
	 */
	void afterTest();

	/**
	 * This method is meant to be executed before a test in run. Typically loads the fixtures. Depending on the context,
	 * it can be annotated with {@code @Before}.
	 */
	void beforeTest();

	/**
	 * Returns an instance of {@link net.cpollet.jixture.dao.UnitDao}.
	 *
	 * @return an instance of {@link net.cpollet.jixture.dao.UnitDao}.
	 */
	UnitDao getUnitDao();

	/**
	 * Returns the {@link org.springframework.orm.hibernate3.HibernateTransactionManager}.
	 * @return the {@link org.springframework.orm.hibernate3.HibernateTransactionManager}.
	 */
	HibernateTransactionManager getTransactionManager();
}
