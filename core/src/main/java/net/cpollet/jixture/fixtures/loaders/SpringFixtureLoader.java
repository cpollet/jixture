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
import net.cpollet.jixture.fixtures.SpringFixture;
import net.cpollet.jixture.utils.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * @author Christophe Pollet
 */
@Component
public class SpringFixtureLoader extends AbstractFixtureLoader<SpringFixture> {
	private static final Logger logger = LoggerFactory.getLogger(SpringFixtureLoader.class);

	@Override
	public Class<? extends Fixture> getLoadableFixture() {
		return SpringFixture.class;
	}

	@Override
	public void load(Fixture fixture, Mode mode) {
		final SpringFixture springFixture = assertCanLoadAndCast(fixture);
		final ApplicationContext context = getApplicationContext(springFixture);

		execute(mode, new Executable() {
			@Override
			public void execute() {
				deleteEntitiesOfClass(getClassesToDelete(springFixture).descendingIterator());
				saveEntities();
			}

			private void saveEntities() {
				for (Class<?> clazz : springFixture.getClasses()) {
					saveBeansOfClass(context, clazz);
				}
			}

			private void saveBeansOfClass(ApplicationContext context, Class<?> clazz) {
				super.saveEntities(context.getBeansOfType(clazz).values().iterator());
			}
		});
	}

	private LinkedList<Class> getClassesToDelete(SpringFixture springFixture) {
		return new LinkedList<Class>(springFixture.getClasses());
	}

	private ApplicationContext getApplicationContext(SpringFixture springFixture) {
		try {
			return new ClassPathXmlApplicationContext(springFixture.getContext());
		}
		catch (Exception e) {
			throw ExceptionUtils.wrapInRuntimeException(e);
		}
	}
}
