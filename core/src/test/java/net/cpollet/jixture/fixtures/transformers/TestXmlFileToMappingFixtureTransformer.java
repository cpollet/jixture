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
import net.cpollet.jixture.tests.mappings.User;
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
	private XmlFileToMappingFixtureTransformer xmlFileToMappingFixtureTransformer;

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
			}
		);

		Mockito.when(conversionServiceFactoryBean.getObject()).thenReturn(genericConversionService);
	}

	@Test
	public void testTransform() throws NoSuchFieldException {
		// GIVEN
		XmlFileFixture xmlFileFixture = new XmlFileFixture("classpath:tests/fixtures/xml-fixture.xml", null);

		Mockito.when(mappingDefinitionHolder.getMappingByTableName("users")).thenReturn(User.class);
		Mockito.when(mappingDefinitionHolder.getFieldByTableAndColumnNames("users", "username")).thenReturn(User.class.getDeclaredField("username"));
		Mockito.when(mappingDefinitionHolder.getFieldByTableAndColumnNames("users", "connections_count")).thenReturn(User.class.getDeclaredField("connectionsCount"));

		// WHEN
		Fixture transformedFixture = xmlFileToMappingFixtureTransformer.transform(xmlFileFixture);

		// THEN
		assertThat(transformedFixture)
				.isInstanceOf(MappingFixture.class);

		MappingFixture mappingFixture = (MappingFixture) transformedFixture;

		assertThat(mappingFixture.getObjects())
				.hasSize(1);
		assertThat(mappingFixture.getObjects().get(0))
				.isInstanceOf(User.class);

		User actualUser = (User) mappingFixture.getObjects().get(0);

		User expectedUser = new User();
		expectedUser.setUsername("username");
		expectedUser.setConnectionsCount(1);

		assertThat(actualUser).isEqualTo(expectedUser);
	}
}
