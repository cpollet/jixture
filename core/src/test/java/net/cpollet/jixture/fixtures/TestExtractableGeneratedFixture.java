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
import net.cpollet.jixture.tests.mappings.Product;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.IsAnything;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestExtractableGeneratedFixture extends TestGeneratedFixture {
	private ExtractableGeneratedFixture extractableGeneratedFixture;

	@Before
	@Override
	public void setUp() {
		extractableGeneratedFixture = new ExtractableGeneratedFixture();
	}

	@Override
	protected GeneratedFixture getGeneratedFixture() {
		return extractableGeneratedFixture;
	}

	@Test
	public void extractEntitiesExtractEntities() {
		// GIVEN
		ExtractableGeneratedFixture.ExtractionResult extractionResult = new ExtractableGeneratedFixture.ExtractionResult();
		extractableGeneratedFixture //
				.addGenerators( //
						GeneratedFixture.from(new Product()) //
								.addFieldGenerator("id", FieldGenerators.in("1", "2")) //
				) //
				.extractEntities(new IsAnything(), extractionResult) //
				.start();

		// WHEN
		while (extractableGeneratedFixture.hasNext()) {
			extractableGeneratedFixture.next();
		}

		// THEN
		assertThat(extractionResult).hasSize(2);
	}

	@Test
	public void extractEntitiesExtractMatchingEntities() {
		// GIVEN
		//noinspection unchecked
		extractableGeneratedFixture //
				.addGenerators( //
						GeneratedFixture.from(new Product()) //
								.addFieldGenerator("id", FieldGenerators.in("1", "2")) //
								.addFieldGenerator("name", FieldGenerators.in("name1", "name2")) //
				) //
				.extractEntities(new AllOf(Arrays.asList(matchProductName("name2"), matchProductId("1")))) //
				.start();

		// WHEN
		while (extractableGeneratedFixture.hasNext()) {
			extractableGeneratedFixture.next();
		}
		ExtractableGeneratedFixture.ExtractionResult extractionResult = extractableGeneratedFixture.getExtractionResult();

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
