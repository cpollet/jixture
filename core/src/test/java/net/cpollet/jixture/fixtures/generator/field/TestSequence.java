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

import net.cpollet.jixture.fixtures.generator.field.Sequence;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestSequence {
	private Sequence sequence;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void incrementMustBeStrictlyPositive() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("increment must be > 0");

		new Sequence(1, 3, 0);
	}

	@Test
	public void stopShouldBeLargerOrEqualToStart() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("stop must be >= start");

		new Sequence(3, 1);
	}

	@Test
	public void firstNextReturnsFirstValue() {
		// GIVEN
		sequence = new Sequence(1, 3);

		assertThat(sequence.next()).isEqualTo(1);
	}

	@Test
	public void secondNextReturnsSecondValue() {
		// GIVEN
		sequence = new Sequence(1, 3);

		assertThat(sequence.next()).isEqualTo(1);
		assertThat(sequence.next()).isEqualTo(2);
	}

	@Test
	public void nextReturnsCorrectValues() {
		// GIVEN
		sequence = new Sequence(1, 3);

		// WHEN + THEN
		assertThat(sequence.next()).isEqualTo(1);
		assertThat(sequence.next()).isEqualTo(2);
		assertThat(sequence.next()).isEqualTo(3);
	}

	@Test
	public void nextThrowsExceptionWhenSequenceFinished() {
		// GIVEN
		sequence = new Sequence(1, 1);

		sequence.next();

		expectedException.expect(NoSuchElementException.class);
		expectedException.expectMessage("Sequence ended");

		sequence.next();
	}

	@Test
	public void nextReturnsCorrectValuesWithIncrement2() {
		// GIVEN
		sequence = new Sequence(1, 3, 2);

		// WHEN + THEN
		assertThat(sequence.next()).isEqualTo(1);
		assertThat(sequence.next()).isEqualTo(3);
	}

	@Test
	public void resetResetsTheSequence() {
		// GIVEN
		sequence = new Sequence(1, 3, 2);

		assertThat(sequence.next()).isEqualTo(1);

		// WHEN
		sequence.reset();

		// THEN
		assertThat(sequence.next()).isEqualTo(1);
	}

	@Test
	public void hasNextReturnsTrueWhenThereAreMoreElements() {
		// GIVEN
		sequence = new Sequence(1, 3);

		// WHEN + THEN
		assertThat(sequence.hasNext()).isTrue();
	}

	@Test
	public void hasNextReturnsFalseWhenThereAreMoreElements() {
		// GIVEN
		sequence = new Sequence(1, 1);

		// WHEN + THEN
		sequence.next();
		assertThat(sequence.hasNext()).isFalse();
	}
}
