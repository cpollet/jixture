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

package net.cpollet.jixture.fixtures.loaders;

import net.cpollet.jixture.fixtures.Fixture;

/**
 * @author Christophe Pollet
 */
public interface FixtureLoader {
	public enum Mode {
		/**
		 * Executes database operation in a new transaction that is committed.
		 */
		COMMIT,
		/**
		 * Executes database operations in the current transaction.
		 */
		NO_COMMIT
	}

	/**
	 * Loads a fixture.
	 *
	 * @param fixture the fixture to load.
	 * @param mode the comit mode to use.
	 */
	void load(Fixture fixture, Mode mode);

	/**
	 * Resets the fixture. After a call to {@code reset}, the fixture is in the same state as it was before the
	 * {@link #load} call.
	 */
	void reset();
}
