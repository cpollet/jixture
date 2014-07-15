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

package net.cpollet.jixture.fixtures;

import net.cpollet.jixture.fixtures.capacities.extraction.ExtractionResult;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractorMatcher;
import net.cpollet.jixture.fixtures.capacities.filtering.Filter;
import org.apache.commons.collections.IteratorUtils;
import org.hamcrest.core.IsAnything;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestMappingFixture {
	@Test
	public void getObjectsReturnsAListWhenConstructedWithVarargs() {
		// GIVEN
		String fixture1 = "Fixture1";
		String fixture2 = "Fixture1";

		// WHEN
		MappingFixture mappingFixture = new MappingFixture(fixture1, fixture2);

		// THEN
		List<Object> fixtures = mappingFixture.getObjects();
		assertThat(fixtures)//
				.hasSize(2)//
				.containsSequence(fixture1, fixture2);
	}

	@Test
	public void getClassesToDeleteReturnsCorrectList() {
		// GIVEN
		String fixture1 = "Fixture1";
		Integer fixture2 = 2;

		// WHEN
		MappingFixture mappingFixture = new MappingFixture(fixture1, fixture2);

		// THEN
		Iterator<Class> classesToDeleteIterator = mappingFixture.getClassesToDeleteIterator();
		List classesToDelete = IteratorUtils.toList(classesToDeleteIterator);
		assertThat(classesToDelete)//
				.hasSize(2)//
				.containsExactly(Integer.class, String.class);
	}

	@Test
	public void getExtractionResultReturnCorrectEntities() {
		// GIVEN
		String fixture1 = "Fixture1";
		Integer fixture2 = 2;


		MappingFixture mappingFixture = new MappingFixture(fixture1, fixture2)
				.addExtractorMatcher(ExtractorMatcher.create(new IsAnything()));

		// WHEN
		ExtractionResult extractionResult = mappingFixture.getExtractionResult();

		// THEN
		assertThat(extractionResult.getEntities()).containsOnly(fixture1, fixture2);
	}

	@Test
	public void entityPassesFilterReturnsTrueWhenNoFilterSet() {
		// GIVEN
		MappingFixture mappingFixture = new MappingFixture();

		// WHEN
		boolean actualValue = mappingFixture.filter(new Object());

		// THEN
		assertThat(actualValue).isTrue();
	}

	@Test
	public void entityPassesFilterReturnsFalseWhenEntityDoesNotPassFilter() {
		// GIVEN
		MappingFixture mappingFixture = new MappingFixture();
		mappingFixture.setFilter(new Filter() {
			@Override
			public boolean filter(Object entity) {
				return false;
			}
		});

		// WHEN
		boolean actualValue = mappingFixture.filter(new Object());

		// THEN
		assertThat(actualValue).isFalse();
	}

	@Test
	public void entityPassesFilterReturnsTrueWhenEntityDoesNotPassFilter() {
		// GIVEN
		MappingFixture mappingFixture = new MappingFixture();
		mappingFixture.setFilter(new Filter() {
			@Override
			public boolean filter(Object entity) {
				return true;
			}
		});

		// WHEN
		boolean actualValue = mappingFixture.filter(new Object());

		// THEN
		assertThat(actualValue).isTrue();
	}
}
