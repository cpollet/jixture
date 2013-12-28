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

package net.cpollet.jixture.asserts.helpers;

import net.cpollet.jixture.helper.MappingField;
import net.cpollet.jixture.utils.ExceptionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Christophe Pollet
 */
public class MappingUtils {
	public static Map<String, ? extends Object> toMap(Object mapping, Collection<MappingField> mappingFields) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		for (MappingField mappingField : mappingFields) {
			setFieldValue(mapping, map, mappingField);
		}

		return map;
	}

	private static void setFieldValue(Object mapping, HashMap<String, Object> map, MappingField mappingField) {
		if (!mappingField.isEmbedded()) {
			setNonEmbeddableFieldValue(map, mapping, mappingField);
		}
		else {
			setEmbeddableFieldValue(map, mapping, mappingField);
		}
	}

	private static void setNonEmbeddableFieldValue(Map map, Object mapping, MappingField mappingField) {
		Object fieldValue = getFieldValue(mapping, mappingField.getField());

		map.put(mappingField.getField().getName(), fieldValue);
	}

	private static Object getFieldValue(Object mapping, Field field) {
		field.setAccessible(true);

		try {
			return field.get(mapping);
		}
		catch (IllegalAccessException e) {
			throw ExceptionUtils.wrapInRuntimeException(e);
		}
		finally {
			field.setAccessible(false);
		}
	}

	private static void setEmbeddableFieldValue(Map map, Object mapping, MappingField mappingField) {
		Object embeddableValue = getFieldValue(mapping, mappingField.getField());
		Object embeddedValue = getFieldValue(embeddableValue, mappingField.getEmbeddableField());

		map.put(mappingField.getField().getName() + "." + mappingField.getEmbeddableField().getName(), embeddedValue);
	}
}
