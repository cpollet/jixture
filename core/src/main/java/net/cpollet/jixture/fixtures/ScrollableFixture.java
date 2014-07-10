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

import java.util.Iterator;
import java.util.List;

/**
 * A fixture that acts like an {@link java.util.Iterator} to return entities. It can be used to load a huge amount of
 * entities or to generate them on the fly without impacting memory.
 *
 * @author Christophe Pollet
 */
public interface ScrollableFixture extends Fixture, Iterator<Object> {
	/**
	 * Returns the list of mapping classes representing the tables to truncate.
	 *
	 * @return the list of mapping classes representing the tables to truncate.
	 */
	List<Class> getClassesToDelete();

	void load(UnitDaoFactory unitDaoFactory);
}
