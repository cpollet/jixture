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

package net.cpollet.jixture.jpa2.helper;

import net.cpollet.jixture.helper.MappingDefinitionHolder;
import net.cpollet.jixture.helper.MappingField;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author Christophe Pollet
 */
@Component
public class Jpa2MappingDefinitionHolder implements MappingDefinitionHolder {
	@Override
	public Collection<Class> getMappingClasses() {
		return null;
	}

	@Override
	public MappingField getMappingFieldByTableAndColumnNames(String tableName, String columnName) {
		return null;
	}

	@Override
	public Class getMappingClassByTableName(String tableName) {
		return null;
	}

	@Override
	public Collection<MappingField> getFieldsByMappingClass(Class mapping) {
		return null;
	}
}
