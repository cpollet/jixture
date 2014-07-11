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

package net.cpollet.jixture.fixtures.extraction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Holds the results of entities extractions. Extractions are composed of collections in which entities are stored.
 * Those collections have names to be able to get entities belonging to a collection.
 *
 * @author Christophe Pollet
 */
public class ExtractionResult {
	private Map<String, Map<Class, List<Object>>> entities;

	public ExtractionResult() {
		this.entities = new HashMap<String, Map<Class, List<Object>>>();
	}

	/**
	 * Add a new entity to an extraction collection.
	 *
	 * @param entity the entity to add.
	 * @param name the name of the collection where this entity should be put.
	 */
	public void add(Object entity, String name) {
		getEntitiesList(entity.getClass(), getNamesMap(name)).add(entity);
	}

	private List<Object> getEntitiesList(Class entityClass, Map<Class, List<Object>> namesMap) {
		if (!namesMap.containsKey(entityClass)) {
			namesMap.put(entityClass, new LinkedList<Object>());
		}

		return namesMap.get(entityClass);
	}

	private Map<Class, List<Object>> getNamesMap(String name) {
		if (!entities.containsKey(name)) {
			//noinspection unchecked
			entities.put(name, (Map) new HashMap<String, List<Object>>());
		}

		return entities.get(name);
	}

	/**
	 * Returns all entities stored in the given collection.
	 *
	 * @param name the collection's name
	 * @return all entities stored in the given collection.
	 */
	public List<Object> getEntities(String name) {
		List<Object> entitiesList = new LinkedList<Object>();

		for (List<Object> objects : getNamesMap(name).values()) {
			entitiesList.addAll(objects);
		}

		return entitiesList;
	}

	/**
	 * Returns all entities of the given type.
	 *
	 * @param entityClass the entities type.
	 * @return all entities of the given type.
	 */
	public List<Object> getEntities(Class entityClass) {
		List<Object> entitiesList = new LinkedList<Object>();

		for (Map<Class, List<Object>> namesMap : entities.values()) {
			entitiesList.addAll(getEntitiesList(entityClass, namesMap));
		}

		return entitiesList;
	}

	/**
	 * Returns all entities of a given type from a particular collection.
	 * @param name the collection's name
	 * @param entityClass the entities type.
	 * @return all entities of a given type from a particular collection.
	 */
	public List<Object> getEntities(String name, Class entityClass) {
		return getEntitiesList(entityClass, getNamesMap(name));
	}

	public List<Object> getEntities() {
		List<Object> entitiesList = new LinkedList<Object>();

		for (Map<Class, List<Object>> namesMap : entities.values()) {
			for (List<Object> objects : namesMap.values()) {
				entitiesList.addAll(objects);
			}
		}

		return entitiesList;
	}
}
