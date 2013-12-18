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

import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.MappingFixture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * @author Christophe Pollet
 */
@Component
public class MappingsFixtureLoader extends AbstractFixtureLoader<MappingFixture> {
	private static final Logger logger = LoggerFactory.getLogger(MappingsFixtureLoader.class);

	@Override
	public Class<? extends Fixture> getLoadableFixture() {
		return MappingFixture.class;
	}

	@Transactional
	@Override
	public void load(Fixture fixture, FixtureLoader.Mode mode) {
		final MappingFixture mappingFixture = assertCanLoadAndCast(fixture);

		execute(mode, new Executable() {
			@Override
			public void execute() {
				deleteEntities();
				saveEntities();
			}

			private void deleteEntities() {
				Iterator<Class> it = getClassesToDelete(mappingFixture).descendingIterator();

				while (it.hasNext()) {
					Class clazz = it.next();
					logger.info("Deleting {}", clazz.getName());
					unitDaoFactory.getUnitDao().deleteAll(clazz);
				}
			}

			private void saveEntities() {
				for (final Object object : mappingFixture.getObjects()) {
					unitDaoFactory.getUnitDao().save(object);
				}
			}
		});
	}

	private LinkedList<Class> getClassesToDelete(MappingFixture mappingFixture) {
		LinkedHashSet<Class> classesToDelete = new LinkedHashSet<Class>();

		for (Object object : mappingFixture.getObjects()) {
			classesToDelete.add(object.getClass());
		}

		return new LinkedList<Class>(classesToDelete);
	}
}
