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
public class TestListSequence {
	private ListSequence listSequence;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void firstCurrentReturnsFirstValueValue() {
		// GIVEN
		listSequence = new ListSequence<Integer>(1, 2, 3);

		// WHEN + THEN
		assertThat(listSequence.current()).isEqualTo(1);
	}

	@Test
	public void nextAfterFirstCurrentReturnsSecondValue() {
		// GIVEN
		listSequence = new ListSequence<Integer>(1, 2, 3);

		// WHEN + THEN
		assertThat(listSequence.current()).isEqualTo(1);
		assertThat(listSequence.next()).isEqualTo(2);
	}

	@Test
	public void currentAfterNextReturnsCurrentValue() {
		// GIVEN
		listSequence = new ListSequence<Integer>(1, 2, 3);

		// WHEN + THEN
		assertThat(listSequence.next()).isEqualTo(1);
		assertThat(listSequence.current()).isEqualTo(1);
	}

	@Test
	public void firstNextReturnsFirstValue() {
		// GIVEN
		listSequence = new ListSequence<Integer>(1, 2, 3);

		// WHEN + THEN
		assertThat(listSequence.next()).isEqualTo(1);
	}

	@Test
	public void secondNextReturnsSecondValue() {
		// GIVEN
		listSequence = new ListSequence<Integer>(1, 2, 3);

		// WHEN + THEN
		assertThat(listSequence.next()).isEqualTo(1);
		assertThat(listSequence.next()).isEqualTo(2);
	}

	@Test
	public void nextReturnsCorrectValues() {
		// GIVEN
		listSequence = new ListSequence<Integer>(1, 2, 3);

		// WHEN + THEN
		assertThat(listSequence.next()).isEqualTo(1);
		assertThat(listSequence.next()).isEqualTo(2);
		assertThat(listSequence.next()).isEqualTo(3);
	}

	@Test
	public void nextThrowsExceptionWhenSequenceFinished() {
		// GIVEN
		listSequence = new ListSequence<Integer>(1);

		// WHEN + THEN
		listSequence.next();

		expectedException.expect(NoSuchElementException.class);
		expectedException.expectMessage("ListSequence{[1]} ended");

		listSequence.next();
	}

	@Test
	public void resetResetsTheSequence() {
		// GIVEN
		listSequence = new ListSequence<Integer>(1, 2, 3);

		assertThat(listSequence.next()).isEqualTo(1);

		// WHEN
		listSequence.reset();

		// THEN
		assertThat(listSequence.next()).isEqualTo(1);

		listSequence.reset();

		assertThat(listSequence.current()).isEqualTo(1);
		assertThat(listSequence.next()).isEqualTo(2);
	}

	@Test
	public void hasNextReturnsTrueWhenThereAreMoreElements() {
		// GIVEN
		listSequence = new ListSequence<Integer>(1, 2, 3);

		// WHEN + THEN
		assertThat(listSequence.hasNext()).isTrue();
	}

	@Test
	public void hasNextReturnsFalseWhenThereAreMoreElements() {
		// GIVEN
		listSequence = new ListSequence<Integer>();

		// WHEN + THEN
		assertThat(listSequence.hasNext()).isFalse();
	}
}
