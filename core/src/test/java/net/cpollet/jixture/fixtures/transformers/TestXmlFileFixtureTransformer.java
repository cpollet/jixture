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
import net.cpollet.jixture.fixtures.XmlFileFixture;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractorMatcher;
import net.cpollet.jixture.fixtures.capacities.filtering.Filter;
import net.cpollet.jixture.fixtures.capacities.filtering.FilterableFixtureProxy;
import net.cpollet.jixture.helper.MappingBuilder;
import net.cpollet.jixture.helper.MappingBuilderFactory;
import net.cpollet.jixture.helper.MappingDefinitionHolder;
import net.cpollet.jixture.helper.MappingField;
import net.cpollet.jixture.tests.mappings.CartEntry;
import net.cpollet.jixture.tests.mappings.PersistentObject;
import net.cpollet.jixture.tests.mappings.Product;
import org.hamcrest.core.IsAnything;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.util.NumberUtils;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestXmlFileFixtureTransformer extends TestFileFixtureTransformer {
	@InjectMocks
	private XmlFileFixtureTransformer xmlFileFixtureTransformer;

	@Test
	public void getFromTypeReturnXmlFileFixture() {
		// GIVEN

		// WHEN
		Class fromType = xmlFileFixtureTransformer.getFromType();

		// THEN
		assertThat(fromType).isEqualTo(XmlFileFixture.class);
	}

	@Test
	public void testTransform() throws NoSuchFieldException {
		// GIVEN
		XmlFileFixture xmlFileFixture = new XmlFileFixture("classpath:tests/fixtures/xml-fixture.xml");
		xmlFileFixture.addExtractorMatcher(ExtractorMatcher.create(new IsAnything()));
		xmlFileFixture.setFilter(new Filter() {
			@Override
			public boolean filter(Object entity) {
				return false;
			}
		});

		// WHEN
		ObjectFixture transformedFixture = xmlFileFixtureTransformer.transform(xmlFileFixture);

		// THEN
		assertCorrectCartEntry(xmlFileFixture, transformedFixture);
	}



	@Test
	public void transformHandlesMappingInheritance() throws NoSuchFieldException {
		// GIVEN
		XmlFileFixture xmlFileFixture = new XmlFileFixture("classpath:tests/fixtures/xml-fixture-with-inheritance.xml");

		// WHEN

		// WHEN
		ObjectFixture transformedFixture = xmlFileFixtureTransformer.transform(xmlFileFixture);

		// THEN
		assertThat(transformedFixture).isInstanceOf(Fixture.class);

		assertThat(transformedFixture.getObjects()).hasSize(1);
		assertThat(transformedFixture.getObjects().get(0)).isInstanceOf(Product.class);

		Product actualProduct = (Product) transformedFixture.getObjects().get(0);

		assertThat(actualProduct.getId()).isEqualTo("id");
		assertThat(actualProduct.getName()).isEqualTo("name");
	}
}
