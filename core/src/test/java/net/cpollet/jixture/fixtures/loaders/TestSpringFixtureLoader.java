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
import net.cpollet.jixture.fixtures.SpringFixture;
import net.cpollet.jixture.tests.mappings.Client;
import net.cpollet.jixture.tests.mappings.User;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

/**
 * @author Christophe Pollet
 */
public class TestSpringFixtureLoader extends AbstractTestFixtureLoader {
	@InjectMocks
	private SpringFixtureLoader springFixtureLoader;

	@Override
	FixtureLoader getFixtureLoader() {
		return springFixtureLoader;
	}

	@Override
	Fixture getLoadableFixture() {
		return new SpringFixture("classpath:/tests/fixtures/spring-fixture.xml", User.class);
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
		springFixtureLoader.load(new SpringFixture("classpath:/tests/fixtures/spring-fixture.xml", User.class, Client.class), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).deleteAll(Client.class);
		inOrder.verify(unitDao).deleteAll(User.class);
	}

	@Test
	public void loadSavesEntitiesInOrder() {
		// GIVEN

		// WHEN
		springFixtureLoader.load(new SpringFixture("classpath:/tests/fixtures/spring-fixture.xml", User.class, Client.class), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).save(Mockito.isA(User.class));
		inOrder.verify(unitDao).save(Mockito.isA(Client.class));
	}

	@Test
	public void loadDeletesOldEntitiesBeforeSavingNewOnes() {
		// GIVEN

		// WHEN
		springFixtureLoader.load(new SpringFixture("classpath:/tests/fixtures/spring-fixture.xml", User.class), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).deleteAll(User.class);
		inOrder.verify(unitDao).save(Mockito.any(User.class));
	}
}
