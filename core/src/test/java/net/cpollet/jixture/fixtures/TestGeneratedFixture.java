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

package net.cpollet.jixture.fixtures;

import net.cpollet.jixture.fixtures.capacities.extraction.ExtractorMatcher;
import net.cpollet.jixture.fixtures.capacities.filtering.Filter;
import net.cpollet.jixture.fixtures.generator.field.FieldGenerators;
import net.cpollet.jixture.fixtures.generator.fixture.SimpleGenerator;
import net.cpollet.jixture.tests.mappings.Product;
import org.apache.commons.collections.IteratorUtils;
import org.hamcrest.core.IsAnything;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Iterator;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestGeneratedFixture {
	private GeneratedFixture generatedFixture;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() {
		generatedFixture = new GeneratedFixture();
	}

	@Test
	public void addGeneratorWhenGeneratorFixtureStartedThrowsAnException() {
		// GIVEN
		generatedFixture.start();

		// THEN
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Generator already started");

		// WHEN
		generatedFixture.addGenerators(new SimpleGenerator(Object.class, 1));
	}

	@Test
	public void hasNextWhenGeneratorFixtureNotStartedThrowsAnException() {
		// GIVEN

		// THEN
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Generator not started");

		// WHEN
		generatedFixture.hasNext();
	}

	@Test
	public void nextWhenGeneratorFixtureNotStartedThrowsAnException() {
		// GIVEN

		// THEN
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Generator not started");

		// WHEN
		generatedFixture.next();
	}

	@Test
	public void nextIteratesOverAllGeneratedObjectsOfAllGenerators() {
		// GIVEN
		generatedFixture //
				.addGenerators( //
						new SimpleGenerator(Object.class, 2), //
						new SimpleGenerator(Object.class, 3)) //
				.start();

		// WHEN
		int actualCount = 0;
		while (generatedFixture.hasNext()) {
			actualCount++;
			generatedFixture.next();
		}

		// THEN
		assertThat(actualCount).isEqualTo(5);
	}

	@Test
	public void getClassesToDeleteReturnsClassesToDeleteOfAllGeneratorsInReverseOrder() {
		// GIVEN
		generatedFixture //
				.addGenerators( //
						new SimpleGenerator(Integer.class, 2), //
						new SimpleGenerator(String.class, 3));

		// WHEN
		Iterator<Class> classesToDeleteIterator = generatedFixture.getClassesToDeleteIterator();

		// THEN
		List classesToDelete = IteratorUtils.toList(classesToDeleteIterator);
		assertThat(classesToDelete) //
				.hasSize(2) //
				.containsExactly(String.class, Integer.class);
	}

	@Test
	public void getExtractionResultReturnCorrectEntities() {
		// GIVEN
		generatedFixture //
				.addGenerators( //
						GeneratedFixture.from(new Product()) //
								.addFieldGenerator("id", FieldGenerators.in("1", "2")) //
				) //
				.addExtractorMatcher(ExtractorMatcher.create("name", new IsAnything())) //
				.start();

		// WHEN
		while (generatedFixture.hasNext()) {
			generatedFixture.next();
		}

		// THEN
		assertThat(generatedFixture.getExtractionResult().getEntities()).hasSize(2);
	}

	@Test
	public void entityPassesFilterReturnsTrueWhenNoFilterSet() {
		// GIVEN

		// WHEN
		boolean actualValue = generatedFixture.filter(new Object());

		// THEN
		assertThat(actualValue).isTrue();
	}

	@Test
	public void entityPassesFilterReturnsFalseWhenEntityDoesNotPassFilter() {
		// GIVEN
		generatedFixture.setFilter(new Filter() {
			@Override
			public boolean filter(Object entity) {
				return false;
			}
		});

		// WHEN
		boolean actualValue = generatedFixture.filter(new Object());

		// THEN
		assertThat(actualValue).isFalse();
	}

	@Test
	public void entityPassesFilterReturnsTrueWhenEntityDoesNotPassFilter() {
		// GIVEN
		generatedFixture.setFilter(new Filter() {
			@Override
			public boolean filter(Object entity) {
				return true;
			}
		});

		// WHEN
		boolean actualValue = generatedFixture.filter(new Object());

		// THEN
		assertThat(actualValue).isTrue();
	}
}
