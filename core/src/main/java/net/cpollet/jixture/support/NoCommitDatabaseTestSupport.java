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

package net.cpollet.jixture.support;

import net.cpollet.jixture.fixtures.loaders.FixtureLoader;

/**
 * Allows to load data in commit mode. This class can be used as a helper class. The
 * {@link net.cpollet.jixture.support.CommitDatabaseTestSupport} contains full exmaple of use.
 *
 * @see net.cpollet.jixture.support.CommitDatabaseTestSupport
 *
 * @author Christophe Pollet
 */
public class NoCommitDatabaseTestSupport extends AbstractTestSupport {
	/**
	 * Returns the commit mode. In this case, always
	 * {@link net.cpollet.jixture.fixtures.loaders.FixtureLoader.Mode#NO_COMMIT}.
	 *
	 * @see net.cpollet.jixture.fixtures.loaders.FixtureLoader.Mode#NO_COMMIT
	 *
	 * @return {@link net.cpollet.jixture.fixtures.loaders.FixtureLoader.Mode#NO_COMMIT}
	 */
	@Override
	public FixtureLoader.Mode getCommitMode() {
		return FixtureLoader.Mode.NO_COMMIT;
	}
}
