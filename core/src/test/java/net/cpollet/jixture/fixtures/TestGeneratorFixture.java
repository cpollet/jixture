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
public class TestGeneratorFixture {
	private GeneratorFixture generatorFixture;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() {
		generatorFixture = new GeneratorFixture();
	}

	@Test
	public void addGeneratorWhenGeneratorFixtureStartedThrowsAnException() {
		// GIVEN
		generatorFixture.start();

		// THEN
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Generator already started");

		// WHEN
		generatorFixture.addGenerators(new SimpleGenerator(Object.class, 1));
	}

	@Test
	public void hasNextWhenGeneratorFixtureNotStartedThrowsAnException() {
		// GIVEN

		// THEN
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Generator not started");

		// WHEN
		generatorFixture.hasNext();
	}

	@Test
	public void nextWhenGeneratorFixtureNotStartedThrowsAnException() {
		// GIVEN

		// THEN
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Generator not started");

		// WHEN
		generatorFixture.next();
	}

	@Test
	public void nextIteratesOverAllGeneratedObjectsOfAllGenerators() {
		// GIVEN
		generatorFixture //
				.addGenerators( //
						new SimpleGenerator(Object.class, 2), //
						new SimpleGenerator(Object.class, 3)) //
				.start();

		// WHEN
		int actualCount = 0;
		while (generatorFixture.hasNext()) {
			actualCount++;
			generatorFixture.next();
		}

		// THEN
		assertThat(actualCount).isEqualTo(5);
	}

	@Test
	public void getClassesToDeleteReturnsClassesToDeleteOfAllGenerators() {
		// GIVEN
		generatorFixture //
				.addGenerators( //
						new SimpleGenerator(Integer.class, 2), //
						new SimpleGenerator(String.class, 3));

		// WHEN
		List<Class> classesToDelete = generatorFixture.getClassesToDelete();

		// THEN
		assertThat(classesToDelete).containsExactly(Integer.class, String.class);
	}

	@Test
	public void extractEntitiesExtractEntities() {
		// GIVEN
		GeneratorFixture.ExtractionResult extractionResult = new GeneratorFixture.ExtractionResult();
		generatorFixture //
				.addGenerators( //
						GeneratorFixture.from(new Product()) //
								.addFieldGenerator("id", FieldGenerators.in("1", "2")) //
				) //
				.extractEntities(new IsAnything(), extractionResult) //
				.start();

		// WHEN
		while (generatorFixture.hasNext()) {
			generatorFixture.next();
		}

		// THEN
		assertThat(extractionResult).hasSize(2);
	}

	@Test
	public void extractEntitiesExtractMatchingEntities() {
		// GIVEN
		//noinspection unchecked
		generatorFixture //
				.addGenerators( //
						GeneratorFixture.from(new Product()) //
								.addFieldGenerator("id", FieldGenerators.in("1", "2")) //
								.addFieldGenerator("name", FieldGenerators.in("name1", "name2")) //
				) //
				.extractEntities(new AllOf(Arrays.asList(matchProductName("name2"), matchProductId("1")))) //
				.start();

		// WHEN
		while (generatorFixture.hasNext()) {
			generatorFixture.next();
		}
		GeneratorFixture.ExtractionResult extractionResult = generatorFixture.getExtractionResult();

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
