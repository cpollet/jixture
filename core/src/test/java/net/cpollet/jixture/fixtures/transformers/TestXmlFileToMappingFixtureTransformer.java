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
import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.fixtures.XmlFileFixture;
import net.cpollet.jixture.helper.MappingDefinitionHolder;
import net.cpollet.jixture.helper.MappingField;
import net.cpollet.jixture.tests.mappings.CartEntry;
import net.cpollet.jixture.tests.mappings.PersistentObject;
import net.cpollet.jixture.tests.mappings.Product;
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
@RunWith(MockitoJUnitRunner.class)
public class TestXmlFileToMappingFixtureTransformer {
	@Mock
	private ConversionServiceFactoryBean conversionServiceFactoryBean;

	@Mock
	private MappingDefinitionHolder mappingDefinitionHolder;

	@InjectMocks
	private XmlFileFixtureTransformer xmlFileFixtureTransformer;

	@Before
	public void setUp() {
		GenericConversionService genericConversionService = new GenericConversionService();

		genericConversionService.addConverter(new Converter<String, Integer>() {
			public Integer convert(String source) {
				if (source.length() == 0) {
					return null;
				}
				return NumberUtils.parseNumber(source, Integer.class);
			}
		});

		Mockito.when(conversionServiceFactoryBean.getObject()).thenReturn(genericConversionService);
	}

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

		Mockito.when(mappingDefinitionHolder.getMappingClassByTableName("cart_entry")).thenReturn(CartEntry.class);

		Mockito.when(mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("cart_entry", "client_id"))//
				.thenReturn(new MappingField(CartEntry.class.getDeclaredField("pk"), CartEntry.CartEntryPk.class.getDeclaredField("clientId")));
		Mockito.when(mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("cart_entry", "product_id"))//
				.thenReturn(new MappingField(CartEntry.class.getDeclaredField("pk"), CartEntry.CartEntryPk.class.getDeclaredField("productId")));
		Mockito.when(mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("cart_entry", "count"))//
				.thenReturn(new MappingField(CartEntry.class.getDeclaredField("count")));

		// WHEN
		Fixture transformedFixture = xmlFileFixtureTransformer.transform(xmlFileFixture);

		// THEN
		assertThat(transformedFixture).isInstanceOf(Fixture.class);

		assertThat(transformedFixture.getObjects()).hasSize(1);
		assertThat(transformedFixture.getObjects().get(0)).isInstanceOf(CartEntry.class);

		CartEntry actualCartEntry = (CartEntry) transformedFixture.getObjects().get(0);

		CartEntry expectedCartEntry = new CartEntry();
		expectedCartEntry.setPk(new CartEntry.CartEntryPk());
		expectedCartEntry.getPk().setClientId(1);
		expectedCartEntry.getPk().setProductId(2);
		expectedCartEntry.setCount(10);

		assertThat(actualCartEntry).isEqualTo(expectedCartEntry);
	}

	@Test
	public void transformHandlesMappingInheritance() throws NoSuchFieldException {
		// GIVEN
		XmlFileFixture xmlFileFixture = new XmlFileFixture("classpath:tests/fixtures/xml-fixture-with-inheritance.xml");

		// WHEN
		Mockito.when(mappingDefinitionHolder.getMappingClassByTableName("product")).thenReturn(Product.class);

		Mockito.when(mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("product", "id"))//
				.thenReturn(new MappingField(PersistentObject.class.getDeclaredField("id")));
		Mockito.when(mappingDefinitionHolder.getMappingFieldByTableAndColumnNames("product", "name"))//
				.thenReturn(new MappingField(Product.class.getDeclaredField("name")));

		// WHEN
		Fixture transformedFixture = xmlFileFixtureTransformer.transform(xmlFileFixture);

		// THEN
		assertThat(transformedFixture).isInstanceOf(Fixture.class);

		assertThat(transformedFixture.getObjects()).hasSize(1);
		assertThat(transformedFixture.getObjects().get(0)).isInstanceOf(Product.class);

		Product actualProduct = (Product) transformedFixture.getObjects().get(0);

		assertThat(actualProduct.getId()).isEqualTo("id");
		assertThat(actualProduct.getName()).isEqualTo("name");
	}
}
