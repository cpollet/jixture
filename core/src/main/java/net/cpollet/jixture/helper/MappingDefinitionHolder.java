package net.cpollet.jixture.helper;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * @author Christophe Pollet
 */
public interface MappingDefinitionHolder {
	Collection<Class> getMappings();

	Field getFieldByTableAndColumnNames(String tableName, String columnName);

	Class getMappingByTableName(String tableName);
}
