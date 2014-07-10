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

import net.cpollet.jixture.fixtures.generator.field.FieldGenerators;
import net.cpollet.jixture.fixtures.generator.fixture.SimpleGenerator;
import net.cpollet.jixture.tests.mappings.Product;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.IsAnything;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
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
	public void getClassesToDeleteReturnsClassesToDeleteOfAllGenerators() {
		// GIVEN
		generatedFixture //
				.addGenerators( //
						new SimpleGenerator(Integer.class, 2), //
						new SimpleGenerator(String.class, 3));

		// WHEN
		List<Class> classesToDelete = generatedFixture.getClassesToDelete();

		// THEN
		assertThat(classesToDelete).containsExactly(Integer.class, String.class);
	}

	@Test
	public void extractEntitiesExtractEntities() {
		// GIVEN
		GeneratedFixture.ExtractionResult extractionResult = new GeneratedFixture.ExtractionResult();
		generatedFixture //
				.addGenerators( //
						GeneratedFixture.from(new Product()) //
								.addFieldGenerator("id", FieldGenerators.in("1", "2")) //
				) //
				.extractEntities(new IsAnything(), extractionResult) //
				.start();

		// WHEN
		while (generatedFixture.hasNext()) {
			generatedFixture.next();
		}

		// THEN
		assertThat(extractionResult).hasSize(2);
	}

	@Test
	public void extractEntitiesExtractMatchingEntities() {
		// GIVEN
		//noinspection unchecked
		generatedFixture //
				.addGenerators( //
						GeneratedFixture.from(new Product()) //
								.addFieldGenerator("id", FieldGenerators.in("1", "2")) //
								.addFieldGenerator("name", FieldGenerators.in("name1", "name2")) //
				) //
				.extractEntities(new AllOf(Arrays.asList(matchProductName("name2"), matchProductId("1")))) //
				.start();

		// WHEN
		while (generatedFixture.hasNext()) {
			generatedFixture.next();
		}
		GeneratedFixture.ExtractionResult extractionResult = generatedFixture.getExtractionResult();

		// THEN
		Product expectedProduct = new Product();
		expectedProduct.setId("1");
		expectedProduct.setName("name2");

		assertThat(extractionResult).hasSize(1);
		assertThat(extractionResult.get(0)).isEqualTo(expectedProduct);
	}

	private Matcher matchProductName(final String name) {
		return new BaseMatcher() {
			@Override
			public boolean matches(Object item) {
				if (!(item instanceof Product)) {
					return false;
				}

				Product p = (Product) item;

				return name.equals(p.getName());
			}

			@Override
			public void describeTo(Description description) {
			}
		};
	}

	private Matcher matchProductId(final String id) {
		return new BaseMatcher() {
			@Override
			public boolean matches(Object item) {
				if (!(item instanceof Product)) {
					return false;
				}

				Product p = (Product) item;

				return id.equals(p.getId());
			}

			@Override
			public void describeTo(Description description) {
			}
		};
	}
}
