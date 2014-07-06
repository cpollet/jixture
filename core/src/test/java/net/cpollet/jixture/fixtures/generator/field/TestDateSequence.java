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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestDateSequence {
	private DateSequence dateSequence;
	private DateTime defaultStart;
	private DateTime defaultStop;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() {
		defaultStart = new DateTime(2014, 7, 6, 19, 43, 0, DateTimeZone.UTC);
		defaultStop = new DateTime(2014, 7, 8, 19, 43, 0, DateTimeZone.UTC);
	}

	@Test
	public void durationMustBeStrictlyPositive() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("duration must be > 0ms");

		new DateSequence(defaultStart, defaultStop, new Duration(0));
	}

	@Test
	public void stopShouldBeLargerOrEqualToStart_duration() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("stop must be >= start");

		new DateSequence(defaultStart, new DateTime(2013, 1, 1, 0, 0, 0), new Duration(1));
	}

	@Test
	public void periodMustBeStrictlyPositive() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("period must be > 0ms");

		new DateSequence(defaultStart, defaultStop, Period.days(-1));
	}

	@Test
	public void stopShouldBeLargerOrEqualToStart_period() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("stop must be >= start");

		new DateSequence(defaultStart, new DateTime(2013, 1, 1, 0, 0, 0), Period.days(1));
	}

	@Test
	public void firstCurrentReturnsFirstValueValue() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStop);

		// WHEN + THEN
		assertThat(dateSequence.current()).isEqualTo(defaultStart.toDate());
	}

	@Test
	public void nextAfterFirstCurrentReturnsSecondValue() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStop);

		// WHEN + THEN
		assertThat(dateSequence.current()).isEqualTo(defaultStart.toDate());
		assertThat(dateSequence.next()).isEqualTo(defaultStart.plus(Period.days(1)).toDate());
	}

	@Test
	public void currentAfterNextReturnsCurrentValue() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStop);

		// WHEN + THEN
		assertThat(dateSequence.next()).isEqualTo(defaultStart.toDate());
		assertThat(dateSequence.current()).isEqualTo(defaultStart.toDate());
	}

	@Test
	public void firstNextReturnsFirstValue() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStop);

		// WHEN + THEN
		assertThat(dateSequence.next()).isEqualTo(defaultStart.toDate());
	}

	@Test
	public void secondNextReturnsSecondValue() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStop);

		// WHEN + THEN
		assertThat(dateSequence.next()).isEqualTo(defaultStart.toDate());
		assertThat(dateSequence.next()).isEqualTo(defaultStart.plus(Period.days(1)).toDate());
	}

	@Test
	public void nextReturnsCorrectValues() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStop);

		// WHEN + THEN
		assertThat(dateSequence.next()).isEqualTo(defaultStart.toDate());
		assertThat(dateSequence.next()).isEqualTo(defaultStart.plus(Period.days(1)).toDate());
		assertThat(dateSequence.next()).isEqualTo(defaultStart.plus(Period.days(2)).toDate());
	}

	@Test
	public void nextThrowsExceptionWhenSequenceFinished() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStart);

		// WHEN + THEN
		dateSequence.next();

		expectedException.expect(NoSuchElementException.class);
		expectedException.expectMessage("DateSequence{2014-07-06T19:43:00.000Z;2014-07-06T19:43:00.000Z;P1D} ended");

		dateSequence.next();
	}

	@Test
	public void nextReturnsCorrectValuesWithIncrement2Days_period() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStop, Period.days(2));

		// WHEN + THEN
		assertThat(dateSequence.next()).isEqualTo(defaultStart.toDate());
		assertThat(dateSequence.next()).isEqualTo(defaultStart.plus(Period.days(2)).toDate());
	}

	@Test
	public void nextReturnsCorrectValuesWithIncrement2Days_duration() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStop, new Duration(2 * 24 * 60 * 60 * 1000));

		// WHEN + THEN
		assertThat(dateSequence.next()).isEqualTo(defaultStart.toDate());
		assertThat(dateSequence.next()).isEqualTo(defaultStart.plus(Period.days(2)).toDate());
	}

	@Test
	public void resetResetsTheSequence() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStop);

		assertThat(dateSequence.next()).isEqualTo(defaultStart.toDate());

		// WHEN
		dateSequence.reset();

		// THEN
		assertThat(dateSequence.next()).isEqualTo(defaultStart.toDate());

		dateSequence.reset();

		assertThat(dateSequence.current()).isEqualTo(defaultStart.toDate());
		assertThat(dateSequence.next()).isEqualTo(defaultStart.plus(Period.days(1)).toDate());
	}

	@Test
	public void hasNextReturnsTrueWhenThereAreMoreElements() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStop);

		// WHEN + THEN
		assertThat(dateSequence.hasNext()).isTrue();
	}

	@Test
	public void hasNextReturnsFalseWhenThereAreMoreElements() {
		// GIVEN
		dateSequence = new DateSequence(defaultStart, defaultStart);

		// WHEN + THEN
		dateSequence.next();
		assertThat(dateSequence.hasNext()).isFalse();
	}
}
