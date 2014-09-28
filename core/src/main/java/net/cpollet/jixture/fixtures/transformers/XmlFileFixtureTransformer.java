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

import net.cpollet.jixture.fixtures.XmlFileFixture;
import net.cpollet.jixture.helper.MappingBuilder;
import net.cpollet.jixture.helper.MappingBuilderFactory;
import net.cpollet.jixture.utils.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Christophe Pollet
 */
@Component
public class XmlFileFixtureTransformer extends FileFixtureTransformer<XmlFileFixture> { //implements FixtureTransformer<XmlFileFixture, ObjectFixture> {
	@Override
	public Class getFromType() {
		return XmlFileFixture.class;
	}

	@Override
	protected List<Object> parse(InputStream inputStream) {
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
		return new XmlFileToMappingsFixtureTransformerSAXHandler(mappingBuilderFactory);
	}

	private static class XmlFileToMappingsFixtureTransformerSAXHandler extends DefaultHandler {
		private final MappingBuilderFactory mappingBuilderFactory;

		private List<Object> objects = new LinkedList<Object>();
		private Object currentObject;

		public XmlFileToMappingsFixtureTransformerSAXHandler(MappingBuilderFactory mappingBuilderFactory) {
			this.mappingBuilderFactory = mappingBuilderFactory;
		}

		@Override
		public void startElement(String uri, String localName, String xmlTableName, Attributes attributes) throws SAXException {
			if ("dataset".equals(xmlTableName)) {
				return;
			}

			MappingBuilder mappingBuilder = mappingBuilderFactory.create(xmlTableName);

			for (int i = 0; i < attributes.getLength(); i++) {
				String xmlColumnName = attributes.getQName(i);

				mappingBuilder.withValue(xmlColumnName, attributes.getValue(i));
			}

			currentObject = mappingBuilder.build();
		}


		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if ("dataset".equals(qName)) {
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
