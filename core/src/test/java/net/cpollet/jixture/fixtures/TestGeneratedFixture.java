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

import net.cpollet.jixture.fixtures.extraction.ExtractorMatcher;
import net.cpollet.jixture.fixtures.generator.field.FieldGenerators;
import net.cpollet.jixture.fixtures.generator.fixture.SimpleGenerator;
import net.cpollet.jixture.tests.mappings.Product;
import org.hamcrest.core.IsAnything;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

	protected GeneratedFixture getGeneratedFixture() {
		return generatedFixture;
	}

	@Test
	public void addGeneratorWhenGeneratorFixtureStartedThrowsAnException() {
		// GIVEN
		getGeneratedFixture().start();

		// THEN
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Generator already started");

		// WHEN
		getGeneratedFixture().addGenerators(new SimpleGenerator(Object.class, 1));
	}

	@Test
	public void hasNextWhenGeneratorFixtureNotStartedThrowsAnException() {
		// GIVEN

		// THEN
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Generator not started");

		// WHEN
		getGeneratedFixture().hasNext();
	}

	@Test
	public void nextWhenGeneratorFixtureNotStartedThrowsAnException() {
		// GIVEN

		// THEN
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage("Generator not started");

		// WHEN
		getGeneratedFixture().next();
	}

	@Test
	public void nextIteratesOverAllGeneratedObjectsOfAllGenerators() {
		// GIVEN
		getGeneratedFixture() //
				.addGenerators( //
						new SimpleGenerator(Object.class, 2), //
						new SimpleGenerator(Object.class, 3)) //
				.start();

		// WHEN
		int actualCount = 0;
		while (getGeneratedFixture().hasNext()) {
			actualCount++;
			getGeneratedFixture().next();
		}

		// THEN
		assertThat(actualCount).isEqualTo(5);
	}

	@Test
	public void getClassesToDeleteReturnsClassesToDeleteOfAllGenerators() {
		// GIVEN
		getGeneratedFixture() //
				.addGenerators( //
						new SimpleGenerator(Integer.class, 2), //
						new SimpleGenerator(String.class, 3));

		// WHEN
		List<Class> classesToDelete = getGeneratedFixture().getClassesToDelete();

		// THEN
		assertThat(classesToDelete).containsExactly(Integer.class, String.class);
	}

	@Test
	public void getExtractionResultReturnCorrectEntities() {
		// GIVEN
		getGeneratedFixture() //
				.addGenerators( //
						GeneratedFixture.from(new Product()) //
								.addFieldGenerator("id", FieldGenerators.in("1", "2")) //
				) //
				.addExtractorMatcher(ExtractorMatcher.create("name", new IsAnything())) //
				.start();

		// WHEN
		while (getGeneratedFixture().hasNext()) {
			getGeneratedFixture().next();
		}

		// THEN
		assertThat(getGeneratedFixture().getExtractionResult().getEntities()).hasSize(2);
	}
}
