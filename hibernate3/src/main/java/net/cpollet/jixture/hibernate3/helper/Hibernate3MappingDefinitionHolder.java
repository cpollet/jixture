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

package net.cpollet.jixture.hibernate3.helper;

import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.helper.MappingDefinitionHolder;
import net.cpollet.jixture.helper.MappingField;
import net.cpollet.jixture.utils.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Christophe Pollet
 */
@Component
public class Hibernate3MappingDefinitionHolder implements MappingDefinitionHolder, InitializingBean {
	@Autowired
	private UnitDaoFactory unitDaoFactory;

	public Collection<Class> annotatedClasses;
	private Map<String, Class> classByTableName;
	private Map<String, Map<String, MappingField>> fieldsByTableAndColumnName;

	@Override
	public Collection<Class> getMappingClasses() {
		if (null == annotatedClasses) {
			Set<String> classNames = unitDaoFactory.getUnitDao().getKnownMappings();
			annotatedClasses = new ArrayList<Class>(classNames.size());

			for (String className : classNames) {
				try {
					annotatedClasses.add(getClass().getClassLoader().loadClass(className));
				}
				catch (ClassNotFoundException e) {
					throw ExceptionUtils.wrapInRuntimeException(e);
				}
			}
		}

		return annotatedClasses;
	}

	@Override
	public Class getMappingClassByTableName(String tableName) {
		initMappingsIfNeeded();

		Class result = classByTableName.get(tableName);

		if (null == result) {
			throw new RuntimeException("Mapping for table " + tableName + " not found");
		}

		return result;
	}

	private void initMappingsIfNeeded() {
		if (mappingsAlreadyCreated()) {
			return;
		}

		classByTableName = new HashMap<String, Class>();
		fieldsByTableAndColumnName = new HashMap<String, Map<String, MappingField>>();

		for (Class mapping : getMappingClasses()) {
			String tableName = getLowercaseTableNameFromMapping(mapping);
			classByTableName.put(tableName, mapping);

			createColumnToFieldMapping(mapping, tableName);
		}
	}

	private boolean mappingsAlreadyCreated() {
		return null != classByTableName && null != fieldsByTableAndColumnName;
	}

	private String getLowercaseTableNameFromMapping(Class mapping) {
		Table annotation = (Table) mapping.getAnnotation(Table.class);

		return annotation.name().toLowerCase();
	}

	private void createColumnToFieldMapping(Class mapping, String tableName) {
		for (Field field : getFieldsUpToObject(mapping)) {
			String columnName = getLowercaseColumnNameFromMapping(field);

			if (null != columnName) {
				getOrCreateFieldsByTableName(tableName).put(columnName, new MappingField(field));
			}
			else {
				createColumnToEmbeddedFieldMapping(tableName, field);
			}
		}
	}

	public static Iterable<Field> getFieldsUpToObject(Class<?> startClass) {

		List<Field> currentClassFields = new ArrayList<Field>();
		currentClassFields.addAll(Arrays.asList(startClass.getDeclaredFields()));
		Class<?> parentClass = startClass.getSuperclass();

		if (null != parentClass) {
			List<Field> parentClassFields = (List<Field>) getFieldsUpToObject(parentClass);
			currentClassFields.addAll(parentClassFields);
		}

		return currentClassFields;
	}

	private String getLowercaseColumnNameFromMapping(Field field) {
		Column column = field.getAnnotation(Column.class);

		if (null == column) {
			column = getColumnFromGetter(field);
		}

		if (null == column) {
			return null;
		}

		if (null == column.name() || "".equals(column.name())) {
			return field.getName(); // FIXME this one may be wrong actually...
		}

		return column.name().toLowerCase();
	}

	private Column getColumnFromGetter(Field field) {
		BeanInfo beanInfo = getBeanInfo(field);

		for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
			if (isGetterForField(pd, field)) {
				return pd.getReadMethod().getAnnotation(Column.class);
			}
		}

		return null;
	}

	private BeanInfo getBeanInfo(Field field) {
		try {
			return Introspector.getBeanInfo(field.getDeclaringClass());
		}
		catch (IntrospectionException e) {
			throw ExceptionUtils.wrapInRuntimeException(e);
		}
	}

	private boolean isGetterForField(PropertyDescriptor pd, Field field) {
		return null != pd.getReadMethod() && pd.getName().equals(field.getName());
	}

	private Map<String, MappingField> getOrCreateFieldsByTableName(String tableName) {
		if (!fieldsByTableAndColumnName.containsKey(tableName)) {
			fieldsByTableAndColumnName.put(tableName, new HashMap<String, MappingField>());
		}

		return fieldsByTableAndColumnName.get(tableName);
	}

	private void createColumnToEmbeddedFieldMapping(String tableName, Field field) {
		EmbeddedId embeddedId = field.getAnnotation(EmbeddedId.class);

		if (null != embeddedId) {
			for (Field embeddedField : field.getType().getDeclaredFields()) {
				String embeddedColumnName = getLowercaseColumnNameFromMapping(embeddedField);
				getOrCreateFieldsByTableName(tableName).put(embeddedColumnName, new MappingField(field, embeddedField));
			}
		}
	}

	@Override
	public MappingField getMappingFieldByTableAndColumnNames(String tableName, String columnName) {
		initMappingsIfNeeded();

		Map<String, MappingField> mappingFields = getFieldsByTableName(tableName);

		return getFieldByColumnName(tableName, columnName, mappingFields);
	}

	private Map<String, MappingField> getFieldsByTableName(String tableName) {
		Map<String, MappingField> mappingFields = fieldsByTableAndColumnName.get(tableName);

		if (null == mappingFields) {
			throw new RuntimeException("Mapping class not found for table " + tableName);
		}

		return mappingFields;
	}

	private MappingField getFieldByColumnName(String tableName, String columnName, Map<String, MappingField> mappingFields) {
		MappingField mappingField = mappingFields.get(columnName);

		if (null == mappingField) {
			throw new RuntimeException("Column " + columnName + " not mapped in mapping class "//
					+ getMappingClassByTableName(tableName).getName() + " for " + tableName);
		}
		return mappingField;
	}

	@Override
	public Collection<MappingField> getFieldsByMappingClass(Class mapping) {
		initMappingsIfNeeded();

		return fieldsByTableAndColumnName.get(getLowercaseTableNameFromMapping(mapping)).values();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(unitDaoFactory, "unitDaoFactory must be set");
	}

	public void setUnitDaoFactory(UnitDaoFactory unitDaoFactory) {
		this.unitDaoFactory = unitDaoFactory;
	}
}
