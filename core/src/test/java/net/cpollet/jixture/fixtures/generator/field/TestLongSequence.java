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
public class TestLongSequence {
	private LongSequence longSequence;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void incrementMustBeStrictlyPositive() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("increment must be > 0");

		new LongSequence(1l, 3l, 0l);
	}

	@Test
	public void stopShouldBeLargerOrEqualToStart() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("stop must be >= start");

		new LongSequence(3l, 1l);
	}

	@Test
	public void firstCurrentReturnsFirstValueValue() {
		// GIVEN
		longSequence = new LongSequence(1l, 3l);

		// WHEN + THEN
		assertThat(longSequence.current()).isEqualTo(1l);
	}

	@Test
	public void nextAfterFirstCurrentReturnsSecondValue() {
		// GIVEN
		longSequence = new LongSequence(1l, 3l);

		// WHEN + THEN
		assertThat(longSequence.current()).isEqualTo(1l);
		assertThat(longSequence.next()).isEqualTo(2l);
	}

	@Test
	public void currentAfterNextReturnsCurrentValue() {
		// GIVEN
		longSequence = new LongSequence(1l, 3l);

		// WHEN + THEN
		assertThat(longSequence.next()).isEqualTo(1l);
		assertThat(longSequence.current()).isEqualTo(1l);
	}

	@Test
	public void firstNextReturnsFirstValue() {
		// GIVEN
		longSequence = new LongSequence(1l, 3l);

		// WHEN + THEN
		assertThat(longSequence.next()).isEqualTo(1l);
	}

	@Test
	public void secondNextReturnsSecondValue() {
		// GIVEN
		longSequence = new LongSequence(1l, 3l);

		// WHEN + THEN
		assertThat(longSequence.next()).isEqualTo(1l);
		assertThat(longSequence.next()).isEqualTo(2l);
	}

	@Test
	public void nextReturnsCorrectValues() {
		// GIVEN
		longSequence = new LongSequence(1l, 3l);

		// WHEN + THEN
		assertThat(longSequence.next()).isEqualTo(1l);
		assertThat(longSequence.next()).isEqualTo(2l);
		assertThat(longSequence.next()).isEqualTo(3l);
	}

	@Test
	public void nextThrowsExceptionWhenSequenceFinished() {
		// GIVEN
		longSequence = new LongSequence(1l, 1l);

		// WHEN + THEN
		longSequence.next();

		expectedException.expect(NoSuchElementException.class);
		expectedException.expectMessage("LongSequence{1;1;1} ended");

		longSequence.next();
	}

	@Test
	public void nextReturnsCorrectValuesWithIncrement2() {
		// GIVEN
		longSequence = new LongSequence(1l, 3l, 2l);

		// WHEN + THEN
		assertThat(longSequence.next()).isEqualTo(1l);
		assertThat(longSequence.next()).isEqualTo(3l);
	}

	@Test
	public void resetResetsTheSequence() {
		// GIVEN
		longSequence = new LongSequence(1l, 3l);

		assertThat(longSequence.next()).isEqualTo(1l);

		// WHEN
		longSequence.reset();

		// THEN
		assertThat(longSequence.next()).isEqualTo(1l);

		longSequence.reset();

		assertThat(longSequence.current()).isEqualTo(1l);
		assertThat(longSequence.next()).isEqualTo(2l);
	}

	@Test
	public void hasNextReturnsTrueWhenThereAreMoreElements() {
		// GIVEN
		longSequence = new LongSequence(1l, 3l);

		// WHEN + THEN
		assertThat(longSequence.hasNext()).isTrue();
	}

	@Test
	public void hasNextReturnsFalseWhenThereAreMoreElements() {
		// GIVEN
		longSequence = new LongSequence(1l, 1l);

		// WHEN + THEN
		longSequence.next();
		assertThat(longSequence.hasNext()).isFalse();
	}
}
