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

package net.cpollet.jixture.fixtures;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestCleaningFixture {
	@Test
	public void getObjectsReturnsAnEmptyList() {
		// GIVEN
		CleaningFixture fixture = new CleaningFixture(String.class, Integer.class);

		// WHEN + THEN
		assertThat(fixture.getObjects()).isEmpty();
	}

	@Test
	public void getClassesToDeleteReturnsClassListWithoutDuplicates() {
		// GIVEN
		CleaningFixture fixture = new CleaningFixture(String.class, Integer.class, String.class);

		// WHEN + THEN
		assertThat(fixture.getClassesToDelete())//
				.hasSize(2)//
				.containsSequence(String.class, Integer.class);
	}
}
