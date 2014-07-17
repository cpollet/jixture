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

package net.cpollet.jixture.fixtures.generator.field;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestStringIndexSequence {
	private StringIndexSequence longStringSequence;

	@Before
	public void setUp() {
		longStringSequence = new StringIndexSequence("_%d_");
	}

	@Test
	public void nextReturnsFirstValue() {
		// GIVEN
		// see @Before

		// WHEN
		String actualValue = longStringSequence.next();

		// THEN
		assertThat(actualValue).isEqualTo("_0_");
	}

	@Test
	public void hasNextReturnsFalse() {
		// GIVEN
		// see @Before

		// WHEN
		boolean actualValue = longStringSequence.hasNext();

		// THEN
		assertThat(actualValue).isFalse();
	}

	@Test
	public void currentReturnsCurrentValue() {
		// GIVEN
		// see @Before

		// WHEN
		String actualValue = longStringSequence.current();

		// THEN
		assertThat(actualValue).isEqualTo("_0_");
	}

	@Test
	public void nextAfterResetIsEverGrowing() {
		// GIVEN
		// see @Before

		// WHEN + THEN
		for (int expectedValue = 0; 5 > expectedValue; expectedValue++) {
			String actualValue = longStringSequence.next();
			assertThat(actualValue).isEqualTo("_" + expectedValue + "_");

			longStringSequence.reset();
		}
	}
}
