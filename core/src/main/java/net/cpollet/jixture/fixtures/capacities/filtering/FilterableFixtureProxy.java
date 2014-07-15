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

import net.cpollet.jixture.fixtures.Fixture;

/**
 * Proxy class for CleanableFixture. It allows the called to not care about whether the
 * {@link net.cpollet.jixture.fixtures.Fixture} actually implements
 * {@link net.cpollet.jixture.fixtures.capacities.filtering.FilterableFixture} but still get consistent result.
 *
 * @author Christophe Pollet
 */
public class FilterableFixtureProxy implements FilterableFixture {
	private Fixture fixture;

	/**
	 * Returns a new instance of {@code FilterableFixtureProxy}.
	 *
	 * @param fixture the fixture instance to proxy.
	 * @return a new instance of {@code FilterableFixture}.
	 */
	public static FilterableFixtureProxy get(Fixture fixture) {
		return new FilterableFixtureProxy(fixture);
	}

	public FilterableFixtureProxy(Fixture fixture) {
		this.fixture = fixture;
	}

	/**
	 * Determines whether the entity must be inserted in database or not.
	 *
	 * @param entity the entity to filter.
	 * @return {@code true} if the entity must be inserted.
	 */
	@Override
	public boolean filter(Object entity) {
		if (fixture instanceof FilterableFixture) {
			return filterableFixture().filter(entity);
		}

		return true;
	}

	private FilterableFixture filterableFixture() {
		return (FilterableFixture) fixture;
	}
}
