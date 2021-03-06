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

package net.cpollet.jixture.hibernate3.dao;

import net.cpollet.jixture.dao.UnitDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Provides a default implementation for {@link net.cpollet.jixture.dao.UnitDao} using Hibernate 3.
 *
 * @author Christophe Pollet
 */
public class Hibernate3UnitDao implements UnitDao {
	private static final Logger logger = LoggerFactory.getLogger(Hibernate3UnitDao.class);

	private Session session;

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		return session.createCriteria(clazz).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOne(Class<T> clazz, Serializable id) {
		return (T) session.get(clazz, id);
	}

	@Override
	public void save(Object object) {
		session.save(object);
	}

	@Override
	public void delete(Object object) {
		session.delete(object);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void deleteAll(Class clazz) {
		session.evict(clazz);
		session.createQuery("delete from " + clazz.getName()).executeUpdate();
		flush();
	}

	@Override
	public void execute(String sqlQuery) {
		session.createSQLQuery(sqlQuery).executeUpdate();
	}

	@Override
	public void flush() {
		logger.debug("flush");
		session.flush();
	}

	@Override
	public void clear() {
		logger.debug("clear");
		session.clear();
	}

	@Override
	public void flushAndClear() {
		flush();
		clear();
	}

	protected void setSession(Session session) {
		this.session = session;
	}

	protected Session getSession() {
		return session;
	}

	protected boolean isSessionOpen() {
		return session.isOpen();
	}

	protected SessionFactory getSessionFactory() {
		return getSession().getSessionFactory();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getKnownMappings() {
		return getSessionFactory().getAllClassMetadata().keySet();
	}
}
