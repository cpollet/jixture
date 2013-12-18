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

import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.fixtures.Fixture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * @author Christophe Pollet
 */
public abstract class AbstractFixtureLoader<T extends Fixture> implements FixtureLoader {
	private static final Logger logger = LoggerFactory.getLogger(AbstractFixtureLoader.class);

	@Autowired
	protected UnitDaoFactory unitDaoFactory;

	@Resource(name = "cleanedEntities")
	protected Set<Class> cleanedEntities;

	@Resource(name = "transactionTemplatesByMode")
	private Map<Class, TransactionTemplate> transactionTemplates;

	protected void cleanEntityIfNeeded(Class clazz) {
		logger.info("Destroying entities of class " + clazz.getName());

		unitDaoFactory.getUnitDao().deleteAll(clazz);
		cleanedEntities.add(clazz);
	}

	@Override
	public boolean canLoad(Fixture fixture) {
		return getLoadableFixture().isAssignableFrom(fixture.getClass());
	}

	protected T assertCanLoadAndCast(Fixture fixture) {
		if (!canLoad(fixture)) {
			throw new IllegalArgumentException("Unable to load fixture of type " + fixture.getClass());
		}

		return (T) fixture;
	}

	@Override
	public void reset() {
		cleanedEntities.clear();
	}

	public void execute(Mode mode, final Executable executable) {
		getTransactionTemplate(mode).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
				executable.execute();
				unitDaoFactory.getUnitDao().flushAndClear();
			}
		});
	}

	private TransactionTemplate getTransactionTemplate(FixtureLoader.Mode mode) {
		return transactionTemplates.get(mode);
	}

	protected interface Executable {
		void execute();
	}
}
