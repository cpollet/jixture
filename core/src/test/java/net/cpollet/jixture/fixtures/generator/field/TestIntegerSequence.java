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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestIntegerSequence {
	private IntegerSequence integerSequence;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void incrementMustBeStrictlyPositive() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("increment must be > 0");

		new IntegerSequence(1, 3, 0);
	}

	@Test
	public void stopShouldBeLargerOrEqualToStart() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("stop must be >= start");

		new IntegerSequence(3, 1);
	}

	@Test
	public void firstCurrentReturnsFirstValueValue() {
		// GIVEN
		integerSequence = new IntegerSequence(1, 3);

		// WHEN + THEN
		assertThat(integerSequence.current()).isEqualTo(1);
	}

	@Test
	public void nextAfterFirstCurrentReturnsSecondValue() {
		// GIVEN
		integerSequence = new IntegerSequence(1, 3);

		// WHEN + THEN
		assertThat(integerSequence.current()).isEqualTo(1);
		assertThat(integerSequence.next()).isEqualTo(2);
	}

	@Test
	public void currentAfterNextReturnsCurrentValue() {
		// GIVEN
		integerSequence = new IntegerSequence(1, 3);

		// WHEN + THEN
		assertThat(integerSequence.next()).isEqualTo(1);
		assertThat(integerSequence.current()).isEqualTo(1);
	}

	@Test
	public void firstNextReturnsFirstValue() {
		// GIVEN
		integerSequence = new IntegerSequence(1, 3);

		// WHEN + THEN
		assertThat(integerSequence.next()).isEqualTo(1);
	}

	@Test
	public void secondNextReturnsSecondValue() {
		// GIVEN
		integerSequence = new IntegerSequence(1, 3);

		// WHEN + THEN
		assertThat(integerSequence.next()).isEqualTo(1);
		assertThat(integerSequence.next()).isEqualTo(2);
	}

	@Test
	public void nextReturnsCorrectValues() {
		// GIVEN
		integerSequence = new IntegerSequence(1, 3);

		// WHEN + THEN
		assertThat(integerSequence.next()).isEqualTo(1);
		assertThat(integerSequence.next()).isEqualTo(2);
		assertThat(integerSequence.next()).isEqualTo(3);
	}

	@Test
	public void nextThrowsExceptionWhenSequenceFinished() {
		// GIVEN
		integerSequence = new IntegerSequence(1, 1);

		// WHEN + THEN
		integerSequence.next();

		expectedException.expect(NoSuchElementException.class);
		expectedException.expectMessage("IntegerSequence{1;1;1} ended");

		integerSequence.next();
	}

	@Test
	public void nextReturnsCorrectValuesWithIncrement2() {
		// GIVEN
		integerSequence = new IntegerSequence(1, 3, 2);

		// WHEN + THEN
		assertThat(integerSequence.next()).isEqualTo(1);
		assertThat(integerSequence.next()).isEqualTo(3);
	}

	@Test
	public void resetResetsTheSequence() {
		// GIVEN
		integerSequence = new IntegerSequence(1, 3);

		assertThat(integerSequence.next()).isEqualTo(1);

		// WHEN
		integerSequence.reset();

		// THEN
		assertThat(integerSequence.next()).isEqualTo(1);

		integerSequence.reset();

		assertThat(integerSequence.current()).isEqualTo(1);
		assertThat(integerSequence.next()).isEqualTo(2);
	}

	@Test
	public void hasNextReturnsTrueWhenThereAreMoreElements() {
		// GIVEN
		integerSequence = new IntegerSequence(1, 3);

		// WHEN + THEN
		assertThat(integerSequence.hasNext()).isTrue();
	}

	@Test
	public void hasNextReturnsFalseWhenThereAreMoreElements() {
		// GIVEN
		integerSequence = new IntegerSequence(1, 1);

		// WHEN + THEN
		integerSequence.next();
		assertThat(integerSequence.hasNext()).isFalse();
	}
}
