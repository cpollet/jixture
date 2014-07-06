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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.NoSuchElementException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestSimpleStringSequence {
	private SimpleStringSequence simpleStringSequence;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private FieldGenerator fieldGenerator;

	@Test
	public void hasNextReturnsFalseWhenUnderlyingFieldGeneratorIsOver() {
		// GIVEN
		Mockito.when(fieldGenerator.hasNext()).thenReturn(false);
		simpleStringSequence = new SimpleStringSequence("", fieldGenerator);

		// WHEN
		boolean actualValue = simpleStringSequence.hasNext();

		// THEN
		assertThat(actualValue).isFalse();
	}

	@Test
	public void hasNextReturnsTrueWhenUnderlyingFieldGeneratorHasNext() {
		// GIVEN
		Mockito.when(fieldGenerator.hasNext()).thenReturn(true);
		simpleStringSequence = new SimpleStringSequence("", fieldGenerator);

		// WHEN
		boolean actualValue = simpleStringSequence.hasNext();

		// THEN
		assertThat(actualValue).isTrue();
	}

	@Test
	public void nextGenerateElements() {
		// GIVEN
		simpleStringSequence = new SimpleStringSequence("_%d_", new IntegerSequence(1, 2));

		// WHEN
		String value1 = simpleStringSequence.next();
		String value2 = simpleStringSequence.next();

		// THEN
		assertThat(value1).isEqualTo("_1_");
		assertThat(value2).isEqualTo("_2_");
	}

	@Test
	public void resetResetsTheSequence() {
		// GIVEN
		simpleStringSequence = new SimpleStringSequence("_%d_", new IntegerSequence(1, 2));
		simpleStringSequence.next();

		// WHEN
		simpleStringSequence.reset();
		String value = simpleStringSequence.next();

		// THEN
		assertThat(value).isEqualTo("_1_");
	}

	@Test
	public void nextThrowsExceptionWhenSequenceFinished() {
		// GIVEN
		Mockito.when(fieldGenerator.hasNext()).thenReturn(false);
		Mockito.when(fieldGenerator.toString()).thenReturn("MockedFieldGenerator");
		simpleStringSequence = new SimpleStringSequence("", fieldGenerator);

		// WHEN + THEN
		expectedException.expect(NoSuchElementException.class);
		expectedException.expectMessage("SimpleStringSequence{'';MockedFieldGenerator} ended");

		simpleStringSequence.next();
	}

	@Test
	public void currentCalledFirstReturnsFirstElement() {
		// GIVEN
		simpleStringSequence = new SimpleStringSequence("_%d_", new IntegerSequence(1, 2));

		// WHEN
		String value = simpleStringSequence.current();

		// THEN
		assertThat(value).isEqualTo("_1_");
	}

	@Test
	public void currentReturnsCurrentElement() {
		// GIVEN
		simpleStringSequence = new SimpleStringSequence("_%d_", new IntegerSequence(1, 2));
		simpleStringSequence.next();

		// WHEN
		String value = simpleStringSequence.current();

		// THEN
		assertThat(value).isEqualTo("_1_");
	}
}
