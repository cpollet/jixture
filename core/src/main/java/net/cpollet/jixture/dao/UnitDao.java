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

package net.cpollet.jixture.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Used for unit/integration testing. Contains useful methods to load fixtures and execute basic operations on database.
 * @author Christophe Pollet
 */
public interface UnitDao {
	/**
	 * Returns a list of stored entities.
	 *
	 * @param clazz the mapping's class.
	 * @return  a list of stored entities.
	 */
	public <T> List<T> getAll(Class<T> clazz);

	/**
	 * Returns one stored entity.
	 *
	 * @param clazz the mapping's class.
	 * @param id the entity's id value.
	 * @return the stored entity
	 */
	public <T> T getOne(Class<T> clazz, Serializable id);

	/**
	 * Saves an entitity to database.
	 *
	 * @param object the entity to save.
	 */
	public void save(Object object);

	/**
	 * Deletes an entity from database.
	 *
	 * @param object the entity to delete.
	 */
	public void delete(Object object);

	/**
	 * Deletes all entities of given mapping.
	 *
	 * @param clazz the mapping's class.
	 */
	@SuppressWarnings("rawtypes")
	public void deleteAll(Class clazz);

	/**
	 * Flushes the session, ie. synchronize the underlying database with persistable state held in memory.
	 */
	public void flush();

	/**
	 * Clears the session, ie. remove all entities from memory cache.
	 */
	public void clear();

	/**
	 * Flushes and clears the session.
	 *
	 * @see #flush()
	 * @see #clear()
	 */
	public void flushAndClear();

	/**
	 * Gets Hibernate's known mappings.
	 *
	 * @return Hibernate's known mappings.
	 */
	public Set<String> getKnownMappings();

	/**
	 * Executes a SQL query against the database.
	 *
	 * @param sqlQuery the SQL query to execute.
	 */
	void execute(String sqlQuery);
}
