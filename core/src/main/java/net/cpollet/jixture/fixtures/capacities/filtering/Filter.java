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

package net.cpollet.jixture.fixtures.capacities.filtering;

/**
 * The Filter filter interface.
 *
 * @see net.cpollet.jixture.fixtures.capacities.filter.FilterableFixture
 *
 * @author Christophe Pollet
 */
public interface Filter {
	/**
	 * This method must return {@code true} when the entity must be saved.
	 *
	 * @param entity the entity to filter.
	 * @return {@code true} if the entity must be saved
	 */
	boolean filter(Object entity);
}
