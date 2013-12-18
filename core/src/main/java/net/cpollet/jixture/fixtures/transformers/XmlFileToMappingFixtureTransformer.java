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
import net.cpollet.jixture.fixtures.FixtureTransformer;
import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.fixtures.XmlFileFixture;
import net.cpollet.jixture.helper.MappingDefinitionHolder;
import net.cpollet.jixture.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Christophe Pollet
 */
@Component
public class XmlFileToMappingFixtureTransformer implements FixtureTransformer<XmlFileFixture> {
	@Autowired
	@Qualifier("mappingConversionServiceFactoryBean")
	private ConversionServiceFactoryBean conversionServiceFactoryBean;

	@Autowired
	private MappingDefinitionHolder mappingDefinitionHolder;

	@Override
	public Fixture transform(XmlFileFixture fixture) {
		List<Object> objects = parse(fixture.getInputStream());

		return new MappingFixture(objects.toArray());
	}

	private List<Object> parse(InputStream inputStream) {
		XmlFileToMappingsFixtureTransformerSAXHandler handler = createSaxHandler();
		SAXParser parser = createSaxParser();

		try {
			parser.parse(inputStream, handler);
		}
		catch (Exception e) {
			throw ExceptionUtils.wrapInRuntimeException(e);
		}

		return handler.getObjects();
	}

	private SAXParser createSaxParser() {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		try {
			return parserFactor.newSAXParser();
		}
		catch (Exception e) {
			throw ExceptionUtils.wrapInRuntimeException(e);
		}
	}

	private XmlFileToMappingsFixtureTransformerSAXHandler createSaxHandler() {
		return new XmlFileToMappingsFixtureTransformerSAXHandler(mappingDefinitionHolder, conversionServiceFactoryBean.getObject());
	}

	private static class XmlFileToMappingsFixtureTransformerSAXHandler extends DefaultHandler {
		private ConversionService conversionService;
		private MappingDefinitionHolder mappingDefinitionHolder;

		private List<Object> objects = new LinkedList<Object>();
		private Object currentObject;

		public XmlFileToMappingsFixtureTransformerSAXHandler(MappingDefinitionHolder mappingDefinitionHolder, ConversionService conversionService) {
			this.conversionService = conversionService;
			this.mappingDefinitionHolder = mappingDefinitionHolder;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (qName.equals("dataset")) {
				return;
			}

			String xmlTableName = qName;

			currentObject = createInstanceOfClass(getMappingClassForTable(xmlTableName));

			for (int i = 0; i < attributes.getLength(); i++) {
				String xmlColumnName = attributes.getQName(i);
				Field field = getFieldForTableAndColumn(xmlTableName, xmlColumnName);

				Object fieldValue = convertValueToFieldType(attributes.getValue(i), field);
				setFieldValue(field, fieldValue);
			}
		}

		private Object createInstanceOfClass(Class type) {
			try {
				return type.newInstance();
			}
			catch (Exception e) {
				throw ExceptionUtils.wrapInRuntimeException(e);
			}
		}

		private Class getMappingClassForTable(String tableName) {
			return mappingDefinitionHolder.getMappingByTableName(tableName.toLowerCase());
		}

		private Field getFieldForTableAndColumn(String xmlLowercaseTableName, String xmlLowercaseColumnName) {
			return mappingDefinitionHolder.getFieldByTableAndColumnNames(xmlLowercaseTableName.toLowerCase(), xmlLowercaseColumnName.toLowerCase());
		}

		private Object convertValueToFieldType(Object value, Field field) {
			return conversionService.convert(value, field.getType());
		}

		private void setFieldValue(Field field, Object fieldValue) {
			try {
				field.setAccessible(true);
				field.set(currentObject, fieldValue);
				field.setAccessible(false);
			}
			catch (Exception e) {
				throw ExceptionUtils.wrapInRuntimeException(e);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equals("dataset")) {
				return;
			}

			objects.add(currentObject);
			currentObject = null;
		}

		public List<Object> getObjects() {
			return objects;
		}
	}
}
