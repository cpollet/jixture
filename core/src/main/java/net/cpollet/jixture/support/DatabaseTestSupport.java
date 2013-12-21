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
import net.cpollet.jixture.fixtures.loaders.FixtureLoaderChain;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

import java.util.Collection;

/**
 * @author Christophe Pollet
 */
public interface DatabaseTestSupport {
	void setFixtures(Collection<Fixture> fixtures);

	Collection<Fixture> getFixtures();

	void setFixtureLoaderChain(FixtureLoaderChain fixtureLoaderChain);

	void afterTest();

	void beforeTest();

	UnitDao getUnitDao();

	HibernateTransactionManager getTransactionManager();
}
