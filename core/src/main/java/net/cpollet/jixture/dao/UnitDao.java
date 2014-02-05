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

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Christophe Pollet
 */
public interface UnitDao {
	public <T> List<T> getAll(Class<T> clazz);

	public <T> T getOne(Class<T> clazz, Serializable id);

	public void save(Object object);

	public void delete(Object object);

	@SuppressWarnings("rawtypes")
	public void deleteAll(Class clazz);

	public void flush();

	public void clear();

	public void flushAndClear();

	public void setSession(Session session);

	public Session getSession();

	public boolean isSessionOpen();

	public SessionFactory getSessionFactory();

	public Set<String> getKnownMappings();

	void execute(String sqlQuery);
}
