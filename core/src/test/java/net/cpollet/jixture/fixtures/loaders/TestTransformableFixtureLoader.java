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
import net.cpollet.jixture.fixtures.TransformableFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestTransformableFixtureLoader {
	@Mock
	private FixtureLoaderChain fixtureLoaderChain;

	@InjectMocks
	private TransformableFixtureLoader transformableFixtureLoader;

	@Before
	public void setUp() {

	}

	@Test
	public void loadTransformsFixtureAndLoadsItThroughLoaderChain() {
		// GIVEN
		final Fixture transformedFixture = new Fixture() {
		};

		TransformableFixture fixture = new TransformableFixture() {
			@Override
			public Fixture getTransformedFixture() {
				return transformedFixture;
			}
		};

		// WHEN
		transformableFixtureLoader.load(fixture, FixtureLoader.Mode.COMMIT);

		// THEN
		Mockito.verify(fixtureLoaderChain).load(transformedFixture, FixtureLoader.Mode.COMMIT);
	}
}
