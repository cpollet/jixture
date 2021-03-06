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

package net.cpollet.jixture.jpa2.dao;

import net.cpollet.jixture.dao.UnitDao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Christophe Pollet
 */
public class Jpa2UnitDao implements UnitDao {
	private EntityManager entityManager;

	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(clazz);

		Root<T> variableRoot = query.from(clazz);
		query.select(variableRoot);

		TypedQuery<T> allQuery = entityManager.createQuery(query);
		return allQuery.getResultList();
	}

	@Override
	public <T> T getOne(Class<T> clazz, Serializable id) {
		return entityManager.find(clazz, id);
	}

	@Override
	public void save(Object object) {
		entityManager.persist(object);
	}

	@Override
	public void delete(Object object) {
		Object mergedObject = entityManager.merge(object);
		entityManager.remove(mergedObject);
	}

	@Override
	public void deleteAll(Class clazz) {
		for (Object entity : getAll(clazz)) {
			delete(entity);
		}
	}

	@Override
	public void flush() {
		entityManager.flush();
	}

	@Override
	public void clear() {
		entityManager.clear();
	}

	@Override
	public void flushAndClear() {
		flush();
		clear();
	}

	@Override
	public Set<String> getKnownMappings() {
		return null;
	}

	@Override
	public void execute(String sqlQuery) {
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.executeUpdate();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public boolean isEntityManagerOpen() {
		return entityManager.isOpen();
	}
}
