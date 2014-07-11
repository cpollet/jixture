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
 * Generates entities by cloning a given template entity. It is possible to update specific entity values using
 * {@link net.cpollet.jixture.fixtures.generator.field.FieldGenerator} instances.
 *
 * <p><b>Requires:</b></p>
 * <ul>
 *     <li><a href="https://code.google.com/p/cloning/">cloning</a></li>
 *     <li><a href="http://commons.apache.org/proper/commons-beanutils/">Commons BeanUtils</a></li>
 * </ul>
 *
 * @see net.cpollet.jixture.fixtures.generator.field.FieldGenerator
 *
 * @author Christophe Pollet
 */
public class TemplateGenerator extends BaseFixtureGenerator {
	private static final Logger logger = LoggerFactory.getLogger(TemplateGenerator.class);

	private Object templateObject;
	private MultiFieldGenerator multiFieldGenerator;

	private Cloner cloner;

	/**
	 * @param templateObject the template entity to clone.
	 */
	public TemplateGenerator(Object templateObject) {
		this.templateObject = templateObject;

		cloner = new Cloner();
		multiFieldGenerator = new MultiFieldGenerator();

		cloner.setDumpClonedClasses(logger.isDebugEnabled());
	}

	/**
	 * Adds a {@link net.cpollet.jixture.fixtures.generator.field.FieldGenerator}.
	 *
	 * @param fieldName the field's name whom value is to be overridden.
	 * @param fieldGenerator the field generator to add
	 * @return the current instance.
	 */
	public TemplateGenerator addFieldGenerator(String fieldName, FieldGenerator fieldGenerator) {
		multiFieldGenerator.addFieldGenerator(fieldName, fieldGenerator);
		return this;
	}

	/**
	 * Returns {@code true} if the generator has more entity to generate (In other words, returns {@code true} if
	 * {@link #next} would return an entity rather than throwing an exception.)
	 *
	 * @return {@code true} if the iteration has more elements.
	 */
	@Override
	public boolean hasNext() {
		return multiFieldGenerator.hasNext();
	}

	/**
	 * Returns the next generated entity.
	 *
	 * @return the next generated entity.
	 *
	 * @throws java.util.NoSuchElementException if the iteration has no more elements.
	 */
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
		return multiFieldGenerator.next();
	}

	/**
	 * Returns the class of generated entities.
	 *
	 * @return the class of generated entities.
	 */
	@Override
	public Class getGeneratedClass() {
		return templateObject.getClass();
	}
}
