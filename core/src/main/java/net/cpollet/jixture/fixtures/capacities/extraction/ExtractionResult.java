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

package net.cpollet.jixture.fixtures.capacities.extraction;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Holds the results of entities extractions. Extractions are composed of collections in which entities are stored.
 * Those collections have names to be able to get entities belonging to a collection.
 *
 * @author Christophe Pollet
 */
public class ExtractionResult {
	Set<Result> results;

	public ExtractionResult() {
		this.results = new HashSet<Result>();
	}

	/**
	 * Add a new entity to an extraction collection.
	 *
	 * @param entity the entity to add.
	 * @param name   the name of the collection where this entity should be put.
	 */
	public void add(Object entity, String name) {
		Result result = new Result(entity, name);
		results.add(result);
	}

	/**
	 * Returns all entities stored in the given collection.
	 *
	 * @param name the collection's name
	 * @return all entities stored in the given collection.
	 */
	public List<Object> getEntities(String name) {
		List<Object> entitiesList = new LinkedList<Object>();

		for (Result result : results) {
			if (result.getResultName().equals(name)) {
				entitiesList.add(result.getObject());
			}
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

		for (Result result : results) {
			if (result.getClazz().equals(entityClass)) {
				entitiesList.add(result.getObject());
			}
		}

		return entitiesList;
	}

	/**
	 * Returns all entities of a given type from a particular collection.
	 *
	 * @param name        the collection's name
	 * @param entityClass the entities type.
	 * @return all entities of a given type from a particular collection.
	 */
	public List<Object> getEntities(String name, Class entityClass) {
		List<Object> entitiesList = new LinkedList<Object>();

		for (Result result : results) {
			if (result.getResultName().equals(name) && result.getClazz().equals(entityClass)) {
				entitiesList.add(result.getObject());
			}
		}

		return entitiesList;
	}

	/**
	 * Returns all entities.
	 *
	 * @return all entities.
	 */
	public List<Object> getEntities() {
		List<Object> entitiesList = new LinkedList<Object>();

		for (Result result : results) {
			entitiesList.add(result.getObject());
		}

		return entitiesList;
	}

	/**
	 * Merges another {@code ExtractionResult} into this one.
	 *
	 * @param extractionResult the other {@code ExtractionResult} to merge.
	 */
	public void merge(ExtractionResult extractionResult) {
		for (Result result : extractionResult.results) {
			add(result.getObject(), result.getResultName());
		}
	}

	private class Result {
		private Object object;
		private Class clazz;
		private String resultName;

		private Result(Object object, String resultName) {
			this.object = object;
			this.clazz = object.getClass();
			this.resultName = resultName;
		}

		public Object getObject() {
			return object;
		}

		public Class getClazz() {
			return clazz;
		}

		public String getResultName() {
			return resultName;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Result)) {
				return false;
			}

			Result result = (Result) o;

			return object == result.object && resultName.equals(result.resultName);
		}

		@Override
		public int hashCode() {
			int result = null != object ? object.hashCode() : 0;
			result = 31 * result + (null != resultName ? resultName.hashCode() : 0);
			return result;
		}
	}
}
