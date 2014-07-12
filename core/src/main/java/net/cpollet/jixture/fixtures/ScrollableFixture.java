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

import java.util.Iterator;
import java.util.LinkedList;

/**
 * A fixture that acts like an {@link java.util.Iterator} to return entities. It can be used to load a huge amount of
 * entities or to generate them on the fly without impacting memory.
 *
 * @author Christophe Pollet
 */
public interface ScrollableFixture<E> extends Fixture {
	/**
	 * Returns {@code true} if the fixture has more elements.
	 * (In other words, returns {@code true} if {@link #next} would
	 * return an element rather than throwing an exception.)
	 *
	 * @return {@code true} if the fixture has more elements
	 */
	boolean hasNext();

	/**
	 * Returns the next element in the fixture.
	 *
	 * @return the next element in the fixture
	 * @throws java.util.NoSuchElementException if the fixture has no more elements
	 */
	E next();

	/**
	 * Returns the list of mapping classes representing the tables to truncate.
	 *
	 * @return the list of mapping classes representing the tables to truncate.
	 */
	LinkedList<Class> getClassesToDelete();
}
