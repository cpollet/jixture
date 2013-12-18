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
	enum Mode {
		COMMIT, NO_COMMIT
	}

	Class<? extends Fixture> getLoadableFixture();

	boolean canLoad(Fixture fixture);

	void load(Fixture fixture, Mode mode);

	void reset();
}
