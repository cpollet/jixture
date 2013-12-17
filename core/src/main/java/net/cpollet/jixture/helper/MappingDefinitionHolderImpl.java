package net.cpollet.jixture.helper;

import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.utils.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Table;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Christophe Pollet
 */
@Component
public class MappingDefinitionHolderImpl implements MappingDefinitionHolder, InitializingBean {
	@Autowired
	private UnitDaoFactory unitDaoFactory;

	public Collection<Class> annotatedClasses;
	private Map<String, Class> classByTableName;
	private Map<String, Field> fieldByTableAndColumnName;

	@Override
	public Collection<Class> getMappings() {
		if (annotatedClasses == null) {
			Set<String> classNames = unitDaoFactory.getUnitDao().getKnownMappings();
			annotatedClasses = new ArrayList<Class>(classNames.size());

			for (String className : classNames) {
				try {
					annotatedClasses.add(getClass().getClassLoader().loadClass(className));
				} catch (ClassNotFoundException e) {
					ExceptionUtils.wrapInRuntimeException(e);
				}
			}
		}

		return annotatedClasses;
	}

	@Override
	public Class getMappingByTableName(String tableName) {
		initMappingsIfNeeded();

		Class result = classByTableName.get(tableName);

		if (result == null) {
			throw new RuntimeException("Mapping for table " + tableName + " not found");
		}

		return result;
	}

	private void initMappingsIfNeeded() {
		if (mappingsAlreadyCreated()) {
			return;
		}

		classByTableName = new HashMap<String, Class>();
		fieldByTableAndColumnName = new HashMap<String, Field>();

		for (Class mapping : getMappings()) {
			String tableName = getLowercaseTableNameFromMapping(mapping);
			classByTableName.put(tableName, mapping);

			createColumnToFieldMapping(mapping, tableName);
		}
	}

	private boolean mappingsAlreadyCreated() {
		return classByTableName != null && fieldByTableAndColumnName != null;
	}

	@Override
	public Field getFieldByTableAndColumnNames(String tableName, String columnName) {
		initMappingsIfNeeded();

		Field field = fieldByTableAndColumnName.get(formatColumnName(tableName, columnName));

		if (field == null) {
			throw new RuntimeException("Column " + columnName + " not mapped in mapping class "
					+ getMappingByTableName(tableName).getName() + " for " + tableName) ;
		}

		return field;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(unitDaoFactory, "unitDaoFactory must be set");
	}

	private String getLowercaseTableNameFromMapping(Class mapping) {
		Table annotation = (Table) mapping.getAnnotation(Table.class);

		return annotation.name().toLowerCase();
	}

	private void createColumnToFieldMapping(Class mapping, String tableName) {
		for (Field field : mapping.getDeclaredFields()) {
			String columnName = getLowercaseColumnNameFromMapping(field);

			if (columnName != null) {
				fieldByTableAndColumnName.put(formatColumnName(tableName, columnName), field);
			}
		}
	}

	private String formatColumnName(String tableName, String columnName) {
		return tableName + "." + columnName;
	}

	private String getLowercaseColumnNameFromMapping(Field field) {
		Column column = field.getAnnotation(Column.class);

		if (column == null) {
			column = getColumnFromGetter(field, column);
		}

		if (column == null) {
			return null;
		}

		if (column.name() == null || "".equals(column.name())) {
			return field.getName(); // FIXME this one may be wrong actually...
		}

		return column.name().toLowerCase();
	}

	private Column getColumnFromGetter(Field field, Column column) {
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
		} catch (IntrospectionException e) {
			throw ExceptionUtils.wrapInRuntimeException(e);
		}
	}

	private boolean isGetterForField(PropertyDescriptor pd, Field field) {
		return pd.getReadMethod() != null && pd.getName().equals(field.getName());
	}

	public void setUnitDaoFactory(UnitDaoFactory unitDaoFactory) {
		this.unitDaoFactory = unitDaoFactory;
	}
}
