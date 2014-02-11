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
import net.cpollet.jixture.fixtures.ObjectFixture;
import net.cpollet.jixture.fixtures.RawFixture;
import net.cpollet.jixture.fixtures.TransformableFixture;
import net.cpollet.jixture.fixtures.transformers.FixtureTransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Christophe Pollet
 */
public class SimpleFixtureLoader implements FixtureLoader, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(SimpleFixtureLoader.class);

	@Autowired
	FixtureTransformerFactory fixtureTransformerFactory;

	@Autowired
	protected UnitDaoFactory unitDaoFactory;

	protected Set<Class> cleanedEntities;

	@Resource(name = "jixture.transactionTemplatesByMode")
	private Map<Mode, TransactionTemplate> transactionTemplates;

	@Override
	public void load(final Fixture fixture, Mode mode) {
		if (fixture instanceof ObjectFixture) {
			load((ObjectFixture) fixture, mode);
		}
		else if (fixture instanceof RawFixture) {
			load((RawFixture) fixture, mode);
		}
		else if (fixture instanceof TransformableFixture) {
			load((TransformableFixture) fixture, mode);
		}
	}

	private void load(TransformableFixture fixture, Mode mode) {
		load(transformToFixture(fixture), mode);
	}

	private Fixture transformToFixture(final TransformableFixture fixture) {
		return fixtureTransformerFactory.getFixtureTransformer(fixture).transform(fixture);
	}

	private void load(final ObjectFixture fixture, Mode mode) {
		execute(mode, new SimpleFixtureLoader.Executable() {
			@Override
			public void execute() {
				deleteEntitiesOfClass(fixture.getClassesToDelete().descendingIterator());
				saveEntities(fixture.getObjects().iterator());
			}
		});
	}

	private void load(final RawFixture fixture, Mode mode) {
		execute(mode, new Executable() {
			@Override
			public void execute() {
				deleteEntitiesOfClass(fixture.getClassesToDelete().descendingIterator());
				fixture.load(unitDaoFactory);
			}
		});
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

	@Override
	public void afterPropertiesSet() throws Exception {
		cleanedEntities = new HashSet<Class>();
	}

	protected abstract class Executable {
		public abstract void execute();

		protected void deleteEntitiesOfClass(Iterator<Class> it) {
			while (it.hasNext()) {
				Class clazz = it.next();

				if (!cleanedEntities.contains(clazz)) {
					logger.info("Deleting {}", clazz.getName());
					unitDaoFactory.getUnitDao().deleteAll(clazz);

					cleanedEntities.add(clazz);
				}
			}
		}

		protected void saveEntities(Iterator<?> it) {
			while (it.hasNext()) {
				unitDaoFactory.getUnitDao().save(it.next());
			}
		}
	}
}
