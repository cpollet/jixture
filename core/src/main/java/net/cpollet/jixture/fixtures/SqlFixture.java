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

package net.cpollet.jixture.fixtures;

import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.fixtures.cleaning.CleanableFixture;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Load data from SQL queries.
 *
 * @author Christophe Pollet
 */
public class SqlFixture implements RawFixture, CleanableFixture {
	private CleaningFixture cleaningFixture;
	private String[] queries;

	/**
	 * @param queries SQL queries to execute.
	 */
	public SqlFixture(String[] queries) {
		this.cleaningFixture = new CleaningFixture();
		this.queries = queries;
	}

	/**
	 * @param queries SQL queries to execute.
	 * @param classes mapping classes representing the tables to truncate before executing the SQL queries.
	 */
	public SqlFixture(String[] queries, Class... classes) {
		this.cleaningFixture = new CleaningFixture(classes);
		this.queries = queries;
	}

	/**
	 * Returns the list of mapping classes representing the tables to truncate.
	 * @return the list of mapping classes representing the tables to truncate.
	 */
	@Override
	public Iterator<Class> getClassesToDeleteIterator() {
		return cleaningFixture.getClassesToDeleteIterator();
	}

	/**
	 * Execute the SQL queries.
	 *
	 * @param unitDaoFactory the {@link net.cpollet.jixture.dao.UnitDaoFactory} to use.
	 */
	@Override
	public void load(UnitDaoFactory unitDaoFactory) {
		for (String query : queries) {
			unitDaoFactory.getUnitDao().execute(query);
		}
	}
}
