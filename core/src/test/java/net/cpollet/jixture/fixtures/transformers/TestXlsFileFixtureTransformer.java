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

import net.cpollet.jixture.fixtures.ObjectFixture;
import net.cpollet.jixture.fixtures.XlsFileFixture;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractorMatcher;
import net.cpollet.jixture.fixtures.capacities.filtering.Filter;
import org.hamcrest.core.IsAnything;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestXlsFileFixtureTransformer extends TestFileFixtureTransformer {
	@InjectMocks
	private XlsFileFixtureTransformer xlsFileFixtureTransformer;

	@Test
	public void getFromTypeReturnXmlFileFixture() {
		// GIVEN

		// WHEN
		Class fromType = xlsFileFixtureTransformer.getFromType();

		// THEN
		assertThat(fromType).isEqualTo(XlsFileFixture.class);
	}

	@Test
	public void testTransform() throws NoSuchFieldException {
		// GIVEN
		XlsFileFixture xlsFileFixture = new XlsFileFixture("classpath:tests/fixtures/xls-fixture.xls");
		xlsFileFixture.addExtractorMatcher(ExtractorMatcher.create(new IsAnything()));
		xlsFileFixture.setFilter(new Filter() {
			@Override
			public boolean filter(Object entity) {
				return false;
			}
		});

		// WHEN
		ObjectFixture transformedFixture = xlsFileFixtureTransformer.transform(xlsFileFixture);

		// THEN
		assertCorrectCartEntry(xlsFileFixture, transformedFixture);
	}
}
