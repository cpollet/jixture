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

import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.ObjectFixture;
import net.cpollet.jixture.fixtures.SpringFixture;
import net.cpollet.jixture.fixtures.extraction.ExtractorMatcher;
import net.cpollet.jixture.fixtures.filter.Filter;
import net.cpollet.jixture.fixtures.filter.FilterableFixtureProxy;
import net.cpollet.jixture.tests.mappings.User;
import org.hamcrest.core.IsAnything;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestSpringFixtureTransformer {
	private SpringFixtureTransformer springFixtureTransformer;

	@Before
	public void setUp() {
		springFixtureTransformer = new SpringFixtureTransformer();
	}

	@Test
	public void getFromTypeReturnXmlFileFixture() {
		// GIVEN

		// WHEN
		Class fromType = springFixtureTransformer.getFromType();

		// THEN
		assertThat(fromType).isEqualTo(SpringFixture.class);
	}

	@Test
	public void testTransform() throws NoSuchFieldException {
		// GIVEN
		SpringFixture springFixture = new SpringFixture("classpath:/tests/fixtures/spring-fixture.xml", User.class);
		springFixture.addExtractorMatcher(ExtractorMatcher.create(new IsAnything()));
		springFixture.setFilter(new Filter() {
			@Override
			public boolean filter(Object entity) {
				return false;
			}
		});

		// WHEN
		ObjectFixture transformedFixture = springFixtureTransformer.transform(springFixture);

		// THEN
		assertThat(transformedFixture).isInstanceOf(Fixture.class);

		assertThat(transformedFixture.getObjects()).hasSize(1);
		assertThat(transformedFixture.getObjects().get(0)).isInstanceOf(User.class);
		assertThat(springFixture.getExtractionResult().getEntities()).hasSize(1);

		assertThat(FilterableFixtureProxy.get(transformedFixture).filter("")).isFalse();
	}
}
