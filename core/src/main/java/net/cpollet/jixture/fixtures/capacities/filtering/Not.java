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
 * Negates a {@link net.cpollet.jixture.fixtures.capacities.filtering.Filter}.
 *
 * @author Christophe Pollet
 */
public class Not implements Filter {
	private Filter filter;

	public Not(Filter filter) {
		this.filter = filter;
	}

	/**
	 * Negates the inner filter value.
	 *
	 * @param entity the entity to filter.
	 * @return the negation of inner filter.
	 */
	@Override
	public boolean filter(Object entity) {
		return !filter.filter(entity);
	}
}
