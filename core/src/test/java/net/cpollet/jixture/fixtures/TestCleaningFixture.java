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

import org.apache.commons.collections.IteratorUtils;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestCleaningFixture {
	@Test
	public void getClassesToDeleteReturnsClassListWithoutDuplicates() {
		// GIVEN
		CleaningFixture fixture = new CleaningFixture(String.class, Integer.class, String.class);

		// WHEN + THEN
		List classesToDelete = IteratorUtils.toList(fixture.getClassesToDeleteIterator());
		assertThat(classesToDelete) //
				.hasSize(2) //
				.containsExactly(String.class, Integer.class);
	}

	@Test
	public void addClassesToDeleteImplementsFluentInterface() {
		// GIVEN
		CleaningFixture fixture = new CleaningFixture();

		// WHEN
		Fixture actualValue = fixture.addClassesToDelete();

		// THEN
		assertThat(actualValue).isSameAs(fixture);
	}
}
