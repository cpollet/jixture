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

package net.cpollet.jixture.fixtures.generator.fixture;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestSimpleGenerator {
	private SimpleGenerator simpleGenerator;

	@Test
	public void hasNextReturnsTrueWhenThereIsMoreObjectsToGenerate() {
		// GIVEN
		simpleGenerator = new SimpleGenerator(Object.class, 1);

		// WHEN
		boolean actualValue = simpleGenerator.hasNext();

		// THEN
		assertThat(actualValue).isTrue();
	}

	@Test
	public void hasNextReturnsFalseWhenThereAreNoMoreObjectsToGenerate() {
		// GIVEN
		simpleGenerator = new SimpleGenerator(Object.class, 0);

		// WHEN
		boolean actualValue = simpleGenerator.hasNext();

		// THEN
		assertThat(actualValue).isFalse();
	}

	@Test
	public void nextGeneratedCorrectAmountOfObjects() {
		// GIVEN
		simpleGenerator = new SimpleGenerator(Object.class, 3);

		// WHEN
		List<Object> generatedObjects = new ArrayList<Object>(3);
		while (simpleGenerator.hasNext()) {
			generatedObjects.add(simpleGenerator.next());
		}

		// THEN
		assertThat(generatedObjects).hasSize(3);
		assertThat(generatedObjects.get(0)).isNotSameAs(generatedObjects.get(1));
		assertThat(generatedObjects.get(0)).isNotSameAs(generatedObjects.get(2));
		assertThat(generatedObjects.get(1)).isNotSameAs(generatedObjects.get(2));
	}

	@Test
	public void getGeneratedClassReturnsCorrectValue() {
		// GIVEN
		simpleGenerator = new SimpleGenerator(Object.class, 0);

		// WHEN
		Class actualValue = simpleGenerator.getGeneratedClass();

		// THEN
		assertThat(actualValue).isEqualTo(Object.class);
	}
}
