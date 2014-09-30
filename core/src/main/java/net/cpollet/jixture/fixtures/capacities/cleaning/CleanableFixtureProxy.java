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

package net.cpollet.jixture.fixtures.capacities.cleaning;

import net.cpollet.jixture.fixtures.Fixture;

import java.util.Collections;
import java.util.Iterator;

/**
 * Proxy class for CleanableFixture. It allows the called to not care about whether the
 * {@link net.cpollet.jixture.fixtures.Fixture} actually implements
 * {@link net.cpollet.jixture.fixtures.capacities.cleaning.CleanableFixture} but still get consistent result.
 *
 * @author Christophe Pollet
 */
public class CleanableFixtureProxy implements CleanableFixture {
	private Fixture fixture;

	/**
	 * Returns a new instance of {@code CleanableFixtureProxy}.
	 *
	 * @param fixture the fixture instance to proxy.
	 * @return a new instance of {@code CleanableFixtureProxy}.
	 */
	public static CleanableFixtureProxy get(Fixture fixture) {
		return new CleanableFixtureProxy(fixture);
	}

	private CleanableFixtureProxy(Fixture fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns an ordered iterator of mapping classes to delete from database.
	 *
	 * @return an ordered iterator of mapping classes to delete from database.
	 */
	@Override
	public Iterator<Class> getClassesToDeleteIterator() {
		if (fixture instanceof CleanableFixture) {
			return cleanableFixture().getClassesToDeleteIterator();
		}

		return Collections.<Class>emptyList().iterator();
	}

	private CleanableFixture cleanableFixture() {
		return (CleanableFixture) fixture;
	}

}
