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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestFixtureLoaderChain {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private List<FixtureLoader> loaders;

	@Mock
	private FixtureLoader matchingFixtureLoader;

	@Mock
	private FixtureLoader nonMatchingFixtureLoader;

	@InjectMocks
	private FixtureLoaderChain fixtureLoaderChain;

	@Before
	public void setUp() throws Exception {
		Mockito.when(matchingFixtureLoader.canLoad(Mockito.any(Fixture.class))).thenReturn(true);
		Mockito.when(nonMatchingFixtureLoader.canLoad(Mockito.any(Fixture.class))).thenReturn(false);
	}

	@Test
	public void loadThrowsExceptionIfNoSuitableLoaderFound() {
		// GIVEN
		registerFixtureLoader();

		// THEN
		expectedException.expect(NoSuchElementException.class);
		expectedException.expectMessage("Unable to find loader for fixture of type " + MappingFixture.class.getName());

		// WHEN
		fixtureLoaderChain.load(new MappingFixture(), FixtureLoader.Mode.COMMIT);
	}

	private void registerFixtureLoader(FixtureLoader... fixtureLoaders) {
		List<FixtureLoader> loaders = new ArrayList<FixtureLoader>();

		loaders.addAll(Arrays.asList(fixtureLoaders));

		Mockito.when(this.loaders.iterator()).thenReturn(loaders.iterator());
	}

	@Test
	public void loadLoadsFixtureUsingAppropriateLoader() {
		// GIVEN
		registerFixtureLoader(nonMatchingFixtureLoader, matchingFixtureLoader);

		Fixture fixture = new MappingFixture();

		// WHEN
		fixtureLoaderChain.load(fixture, FixtureLoader.Mode.COMMIT);

		// THEN
		Mockito.verify(nonMatchingFixtureLoader, Mockito.never()).load(Mockito.any(Fixture.class), Mockito.any(FixtureLoader.Mode.class));
		Mockito.verify(matchingFixtureLoader).load(fixture, FixtureLoader.Mode.COMMIT);
	}

	@Test
	public void resetResetsAllRegisteredLoaders() {
		// GIVEN
		registerFixtureLoader(nonMatchingFixtureLoader, matchingFixtureLoader);

		// WHEN
		fixtureLoaderChain.resetLoaders();

		// THEN
		Mockito.verify(nonMatchingFixtureLoader).reset();
		Mockito.verify(matchingFixtureLoader).reset();
	}
}
