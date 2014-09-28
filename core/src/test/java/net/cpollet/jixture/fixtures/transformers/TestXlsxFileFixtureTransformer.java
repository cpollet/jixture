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

package net.cpollet.jixture.fixtures.transformers;

import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.ObjectFixture;
import net.cpollet.jixture.fixtures.XlsxFileFixture;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractorMatcher;
import net.cpollet.jixture.fixtures.capacities.filtering.Filter;
import net.cpollet.jixture.fixtures.capacities.filtering.FilterableFixtureProxy;
import net.cpollet.jixture.helper.MappingBuilder;
import net.cpollet.jixture.helper.MappingField;
import net.cpollet.jixture.tests.mappings.CartEntry;
import org.hamcrest.core.IsAnything;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestXlsxFileFixtureTransformer extends TestFileFixtureTransformer {
	@InjectMocks
	private XlsxFileFixtureTransformer xlsxFileFixtureTransformer;

	@Test
	public void getFromTypeReturnXlsxFileFixture() {
		// GIVEN

		// WHEN
		Class fromType = xlsxFileFixtureTransformer.getFromType();

		// THEN
		assertThat(fromType).isEqualTo(XlsxFileFixture.class);
	}

	@Test
	public void testTransform() throws NoSuchFieldException {
		// GIVEN
		XlsxFileFixture xlsxFileFixture = new XlsxFileFixture("classpath:tests/fixtures/xlsx-fixture.xlsx");
		xlsxFileFixture.addExtractorMatcher(ExtractorMatcher.create(new IsAnything()));
		xlsxFileFixture.setFilter(new Filter() {
			@Override
			public boolean filter(Object entity) {
				return false;
			}
		});

		// WHEN
		ObjectFixture transformedFixture = xlsxFileFixtureTransformer.transform(xlsxFileFixture);

		// THEN
		assertCorrectCartEntry(xlsxFileFixture, transformedFixture);
	}
}
