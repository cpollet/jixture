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

package net.cpollet.jixture.fixtures.generator.fixture;

import com.rits.cloning.Cloner;
import net.cpollet.jixture.fixtures.generator.field.FieldGenerator;
import net.cpollet.jixture.fixtures.generator.field.MultiFieldGenerator;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Christophe Pollet
 */
public class TemplateGenerator implements FixtureGenerator {
	private static final Logger logger = LoggerFactory.getLogger(TemplateGenerator.class);

	private Object templateObject;
	private MultiFieldGenerator multiFieldGenerator;

	private Cloner cloner;

	public TemplateGenerator(Object templateObject) {
		this.templateObject = templateObject;

		cloner = new Cloner();
		multiFieldGenerator = new MultiFieldGenerator();

		cloner.setDumpClonedClasses(logger.isDebugEnabled());
	}

	public TemplateGenerator addFieldGenerator(String fieldName, FieldGenerator fieldGenerator) {
		multiFieldGenerator.addFieldGenerator(fieldName, fieldGenerator);
		return this;
	}

	@Override
	public boolean hasNext() {
		return multiFieldGenerator.hasNext();
	}

	@Override
	public Object next() {
		Object object = cloner.deepClone(templateObject);

		Map<String, Object> values = nextValues();

		for (Map.Entry<String, Object> entry : values.entrySet()) {
			try {
				PropertyUtils.setProperty(object, entry.getKey(), entry.getValue());
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return object;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> nextValues() {
		return (Map<String, Object>) multiFieldGenerator.next();
	}

	@Override
	public Class getGeneratedClass() {
		return templateObject.getClass();
	}
}
