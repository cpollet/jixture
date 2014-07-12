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

import java.util.LinkedList;
import java.util.List;

/**
 * Loads entities instance into database.
 *
 * @author Christophe Pollet
 */
public interface ObjectFixture<T> extends ScrollableFixture<T> {
	/**
	 * Adds object to the list of object to load into database.
	 *
	 * @param objects the objects to add.
	 *
	 * @return the current fixture instance.
	 */
	Fixture addObjects(T... objects);

	/**
	 * Returns the list of entities to load into database.
	 *
	 * @return the list of entities to load into database.
	 */
	List<T> getObjects();
}
