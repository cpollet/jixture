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

package net.cpollet.jixture.fixtures.transformers;

import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.fixtures.ObjectFixture;
import net.cpollet.jixture.fixtures.SpringFixture;
import net.cpollet.jixture.utils.ExceptionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Christophe Pollet
 */
@Component
public class SpringFixtureTransformer implements FixtureTransformer<SpringFixture, ObjectFixture> {
	@Override
	public Class getFromType() {
		return SpringFixture.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectFixture transform(SpringFixture fixture) {
		final ApplicationContext context = getApplicationContext(fixture);

		MappingFixture mappingFixture = new MappingFixture();

		for (Class<?> clazz : fixture.getClasses()) {
			mappingFixture.addObjects(context.getBeansOfType(clazz).values().toArray());
		}

		fixture.populateExtractionResult(mappingFixture.getObjects());
		mappingFixture.setFilter(fixture.getFilter());

		return mappingFixture;
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
