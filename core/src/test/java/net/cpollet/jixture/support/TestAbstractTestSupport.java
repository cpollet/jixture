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

package net.cpollet.jixture.support;

import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.loaders.FixtureLoader;
import org.junit.Test;

import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestAbstractTestSupport {
	@Test
	public void addSpringContextFixtureLoadsContextAndAddFixturesToTheList() {
		// GIVEN
		DatabaseTestSupport testSupport = new ConcreteTestSupport();

		// WHEN
		testSupport.addFixtures("classpath:tests/fixtures/fixtures-context.xml");

		// THEN
		Collection<Fixture> fixtures = testSupport.getFixtures();
		assertThat(fixtures.size()).isEqualTo(4);
	}

	private class ConcreteTestSupport extends BaseDatabaseTestSupport {
		@Override
		protected FixtureLoader.Mode getCommitMode() {
			return null;
		}
	};
}
