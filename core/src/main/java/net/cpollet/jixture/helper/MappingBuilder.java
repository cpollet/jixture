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

package net.cpollet.jixture.helper;

import net.cpollet.jixture.utils.ExceptionUtils;
import org.springframework.core.convert.ConversionService;

import java.lang.reflect.Field;

/**
 * @author Christophe Pollet
 */
public class MappingBuilder {
	private final ConversionService conversionService;
	private final MappingDefinitionHolder mappingDefinitionHolder;
	private final String tableName;
	private final Object mapping;

	private MappingBuilder(String tableName, MappingDefinitionHolder mappingDefinitionHolder, ConversionService conversionService) {
		this.mappingDefinitionHolder = mappingDefinitionHolder;
		this.conversionService = conversionService;
		this.tableName = tableName.toLowerCase();
		this.mapping = createInstanceOfClass(getMappingClassForTable(this.tableName));
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
		return mappingDefinitionHolder.getMappingClassByTableName(tableName);
	}

	public static MappingBuilder createMapping(String tableName, MappingDefinitionHolder mappingDefinitionHolder, ConversionService conversionService) {
		return new MappingBuilder(tableName, mappingDefinitionHolder, conversionService);
	}

	public MappingBuilder withValue(String columnName, Object value) {
		MappingField mappingField = getMappingFieldForTableAndColumn(columnName);

		if (mappingField.isEmbedded()) {
			handleEmbeddedAttribute(mappingField, value);
		}
		else {
			handleAttribute(mappingField, value);
		}

		return this;
	}

	private MappingField getMappingFieldForTableAndColumn(String columnName) {
		return mappingDefinitionHolder.getMappingFieldByTableAndColumnNames(tableName, columnName.toLowerCase());
	}

	private void handleAttribute(MappingField mappingField, Object value) {
		Field field = mappingField.getField();

		Object fieldValue = convertValueToFieldType(value, field);

		setFieldValue(mapping, field, fieldValue);
	}

	private Object convertValueToFieldType(Object value, Field field) {
		return conversionService.convert(value, field.getType());
	}

	private void setFieldValue(Object object, Field field, Object fieldValue) {
		field.setAccessible(true);

		try {
			field.set(object, fieldValue);
		}
		catch (Exception e) {
			throw ExceptionUtils.wrapInRuntimeException(e);
		}
		finally {
			field.setAccessible(false);
		}
	}

	private void handleEmbeddedAttribute(MappingField mappingField, Object value) {
		Field field = mappingField.getField();
		Field embeddedField = mappingField.getEmbeddableField();

		Object fieldValue = convertValueToFieldType(value, embeddedField);
		Object embeddedFieldValue = getEmbeddedFieldValue(mapping, field);

		setFieldValue(embeddedFieldValue, embeddedField, fieldValue);
	}

	private Object getEmbeddedFieldValue(Object object, Field field) {
		Object value = getFieldValue(object, field);

		if (null == value) {
			value = createInstanceOfClass(field.getType());
			setFieldValue(object, field, value);
		}

		return value;
	}

	private Object getFieldValue(Object object, Field field) {
		field.setAccessible(true);

		try {
			return field.get(object);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		finally {
			field.setAccessible(false);
		}
	}

	public Object build() {
		return mapping;
	}
}
