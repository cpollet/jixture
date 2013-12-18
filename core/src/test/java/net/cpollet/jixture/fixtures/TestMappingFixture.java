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

package net.cpollet.jixture.fixtures;

import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestMappingFixture {
	@Test
	public void getObjectsReturnsAListWhenConstructedWithVarargs() {
		// GIVEN
		String fixture1 = "Fixture1";
		String fixture2 = "Fixture1";

		// WHEN
		MappingFixture mappingFixture = new MappingFixture(fixture1, fixture2);

		// THEN
		List<Object> fixtures = mappingFixture.getObjects();
		assertThat(fixtures)
				.hasSize(2)
				.containsSequence(fixture1, fixture2);
	}
}
