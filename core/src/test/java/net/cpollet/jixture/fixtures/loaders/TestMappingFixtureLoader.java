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
import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.tests.mappings.Client;
import net.cpollet.jixture.tests.mappings.User;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

/**
 * @author Christophe Pollet
 */
public class TestMappingFixtureLoader extends AbstractTestFixtureLoader {
	@InjectMocks
	private MappingsFixtureLoader mappingsFixtureLoader;

	@Override
	FixtureLoader getFixtureLoader() {
		return mappingsFixtureLoader;
	}

	@Override
	Fixture getLoadableFixture() {
		return new MappingFixture();
	}

	@Override
	Fixture getNonLoadableFixture() {
		return new Fixture() {
		};
	}

	@Test
	public void loadCleansEntitiesInReverseOrder() {
		// GIVEN

		// WHEN
		mappingsFixtureLoader.load(new MappingFixture(//
				new User(),//
				new Client()), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).deleteAll(Client.class);
		inOrder.verify(unitDao).deleteAll(User.class);
	}

	@Test
	public void loadSavesEntitiesInOrder() {
		// GIVEN
		User user = new User();
		Client client = new Client();

		// WHEN
		mappingsFixtureLoader.load(new MappingFixture(user, client), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).save(user);
		inOrder.verify(unitDao).save(client);
	}

	@Test
	public void loadDeletesOldEntitiesBeforeSavingNewOnes() {
		// GIVEN
		User user = new User();

		// WHEN
		mappingsFixtureLoader.load(new MappingFixture(user), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).deleteAll(User.class);
		inOrder.verify(unitDao).save(user);
	}
}
