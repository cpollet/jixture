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

package net.cpollet.jixture.fixtures.generator.field;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Generates a list of field name/field value pairs. It is backed by several instances of {@code FieldGenerator} and
 * generates all combinations of their generated values:
 * <pre>
 *     generator = new MultiFieldGenerator();
 *     generator.addFieldGenerator("f1", new IntegerSequence(1, 2));
 *     generator.addFieldGenerator("f2", new IntegerSequence(3, 4));
 * </pre>
 * would produce the following pairs:
 * <ul>
 *     <li>f1=1, f2=3</li>
 *     <li>f1=1, f2=4</li>
 *     <li>f1=2, f2=3</li>
 *     <li>f1=2, f2=4</li>
 *  </ul>
 *
 * @author Christophe Pollet
 */
public class MultiFieldGenerator extends BaseFieldGenerator<Map<String, Object>> {
	private List<NamedFieldGenerator> fieldGenerators;

	private Map<String, Object> currentValue;

	public MultiFieldGenerator() {
		this.fieldGenerators = new LinkedList<NamedFieldGenerator>();
		currentValue = null;
	}

	public MultiFieldGenerator addFieldGenerator(String fieldName, FieldGenerator fieldGenerator) {
		fieldGenerators.add(new NamedFieldGenerator(fieldName, fieldGenerator));
		return this;
	}

	@Override
	public void reset() {
		for (NamedFieldGenerator fieldGenerator : fieldGenerators) {
			fieldGenerator.getFieldGenerator().reset();
		}
	}

	@Override
	public boolean hasNext() {
		for (NamedFieldGenerator fieldGenerator : fieldGenerators) {
			if (fieldGenerator.getFieldGenerator().hasNext()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Map<String, Object> next() {
		if (!hasNext()) {
			throw new NoSuchElementException("No more field value to generate");
		}

		moveGeneratorsToNextValue();
		buildNextValue();

		return currentValue;
	}

	private void buildNextValue() {
		currentValue = new HashMap<String, Object>();
		for (NamedFieldGenerator namedFieldGenerator : fieldGenerators) {
			String fieldName = namedFieldGenerator.getFieldName();
			Object value = namedFieldGenerator.getFieldGenerator().current();

			currentValue.put(fieldName, value);
		}
	}

	private void moveGeneratorsToNextValue() {
		for (int i = fieldGenerators.size() - 1; 0 <= i; i--) {
			if (fieldGenerators.get(i).getFieldGenerator().hasNext()) {
				fieldGenerators.get(i).getFieldGenerator().next();
				break;
			} else {
				fieldGenerators.get(i).getFieldGenerator().reset();
			}
		}
	}

	@Override
	public Map<String, Object> current() {
		if (null == currentValue) {
			return next();
		}

		return currentValue;
	}

	private class NamedFieldGenerator {
		private String fieldName;
		private FieldGenerator fieldGenerator;

		private NamedFieldGenerator(String fieldName, FieldGenerator fieldGenerator) {
			this.fieldName = fieldName;
			this.fieldGenerator = fieldGenerator;
		}

		public String getFieldName() {
			return fieldName;
		}

		public FieldGenerator getFieldGenerator() {
			return fieldGenerator;
		}
	}
}
