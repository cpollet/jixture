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

import net.cpollet.jixture.fixtures.BaseObjectFixture;
import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.XmlFileFixture;
import net.cpollet.jixture.fixtures.extraction.ExtractionResult;
import net.cpollet.jixture.fixtures.extraction.ExtractorMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestFixtureTransformerFactory {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private List<FixtureTransformer> transformers;

	@InjectMocks
	private FixtureTransformerFactory fixtureTransformerFactory;

	private List<FixtureTransformer> transformersList;

	@Test
	public void getFixtureTransformerThrowsExceptionWhenNoTransformerDefined() throws Exception {
		// GIVEN
		initializeTransformers();

		// THEN
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No fixture transformer defined for " + XmlFileFixture.class.getName());

		// WHEN
		fixtureTransformerFactory.getFixtureTransformer(new XmlFileFixture("classpath:tests/fixtures/xml-fixture.xml"));
	}

	private void initializeTransformers(FixtureTransformer... fixtureTransformers) throws Exception {
		transformersList = Arrays.asList(fixtureTransformers);

		Mockito.when(transformers.iterator()).thenReturn(transformersList.iterator());

		fixtureTransformerFactory.afterPropertiesSet();
	}

	@Test
	public void getFixtureTransformerReturnsCorrectTransformerIfDefined() throws Exception {
		// GIVEN
		XmlFileFixtureTransformer expectedFixtureTransformer = new XmlFileFixtureTransformer();
		initializeTransformers(expectedFixtureTransformer);

		// WHEN
		FixtureTransformer actualFixtureTransformer = fixtureTransformerFactory.getFixtureTransformer(new XmlFileFixture("classpath:tests/fixtures/xml-fixture.xml"));

		// THEN
		assertThat(actualFixtureTransformer).isEqualTo(expectedFixtureTransformer);
	}

	@Test
	public void getFixtureTransformerReturnsIdentityTransformerForFixtureInstances() throws Exception {
		// GIVEN
		initializeTransformers();

		Fixture fixture = new BaseObjectFixture() {
			@Override
			public Fixture addObjects(Object... objects) {
				return this;
			}

			@Override
			public List<Object> getObjects() {
				return null;
			}
		};

		// WHEN
		FixtureTransformer fixtureTransformer = fixtureTransformerFactory.getFixtureTransformer(fixture);
		Fixture actualFixture = fixtureTransformer.transform(fixture);

		// THEN
		assertThat(actualFixture).isSameAs(fixture);
	}
}
