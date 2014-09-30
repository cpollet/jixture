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

import net.cpollet.jixture.fixtures.capacities.cleaning.CleanableFixture;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * A fixture that only truncates tables but does not insert an data into them.
 *
 * @author Christophe Pollet
 */
public class CleaningFixture implements Fixture, CleanableFixture {
	private Set<Class> classes;

	/**
	 * @param classesToDelete a list of the Hibernate mappings representing the tables to truncate.
	 */
	public CleaningFixture(Class... classesToDelete) {
		classes = new LinkedHashSet<Class>();
		addClassesToDelete(classesToDelete);
	}

	/**
	 * Add Hibernate mappings to the list of mappings representing the tables to truncate.
	 *
	 * @param classesToDelete the mappings to add.
	 * @return the current fixture instance.
	 */
	public Fixture addClassesToDelete(Class... classesToDelete) {
		Collections.addAll(classes, classesToDelete);

		return this;
	}

	/**
	 * Returns an ordered iterator of mapping classes to delete from database.
	 *
	 * @return an ordered iterator of mapping classes to delete from database.
	 */
	@Override
	public Iterator<Class> getClassesToDeleteIterator() {
		return new LinkedList<Class>(classes).iterator();
	}
}
