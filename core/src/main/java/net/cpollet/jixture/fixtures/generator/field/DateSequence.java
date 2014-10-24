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

import net.cpollet.jixture.utils.AssertionUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadablePeriod;

import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Generates a sequence of {@link java.util.Date}.
 *
 * <p><b>Requires</b></p>
 * <ul>
 *     <li><a href="http://www.joda.org/joda-time/">Joda Time</a></li>
 * </ul>
 *
 * @author Christophe Pollet
 */
public class DateSequence extends BaseFieldGenerator<Date> {
	private DateTime start;
	private DateTime stop;
	private DateTime current;
	private DateTime next;
	private ReadableDuration duration;
	private ReadablePeriod period;

	/**
	 * Build a sequence that will return values between {@code start} and {@code end} (inclusive) with an increment of
	 * 1 day.
	 *
	 * @see #DateSequence(org.joda.time.DateTime, org.joda.time.DateTime, org.joda.time.ReadablePeriod)
	 *
	 * @param start first value in sequence, inclusive.
	 * @param stop last element in sequence, inclusive.
	 */
	public DateSequence(DateTime start, DateTime stop) {
		this(start, stop, Period.days(1));
	}

	/**
	 * Build a sequence that will return values between {@code start} and {@code end} (inclusive) with an increment of
	 * {@code increment}. For instance, in:
	 * <pre>
	 *     s1 = new DateSequence(
	 *     	new DateTime(1, 1, 1, 0, 0, 0),
	 *     	new DateTime(1, 1, 3, 0, 0, 0),
	 *     	new Duration(24 * 60 * 60 * 1000)
	 *     );
	 *     s1 = new DateSequence(
	 *     	new DateTime(1, 1, 1, 0, 0, 0),
	 *     	new DateTime(1, 1, 5, 0, 0, 0),
	 *     	new Duration(2 * 24 * 60 * 60 * 1000)
	 *     );
	 * </pre>
	 * {@code s1} will generate the following dates: {@code 1-1-1T0:0:0}, {@code 1-1-2T0:0:0}, {@code 1-1-3T0:0:0} and
	 * {@code s2} will generate {@code 1-1-1T0:0:0}, {@code 1-1-3T0:0:0}, {@code 1-1-5T0:0:0}.
	 *
	 * @param start first value in sequence, inclusive.
	 * @param stop last element in sequence, inclusive.
	 * @param duration increment.
	 *
	 * @throws java.lang.IllegalArgumentException if start is less than stop or if increment is less than or equal to
	 * 0ms.
	 */
	public DateSequence(DateTime start, DateTime stop, ReadableDuration duration) {
		AssertionUtils.assertTrue(duration.isLongerThan(Duration.ZERO), "duration must be > 0ms");
		AssertionUtils.assertTrue(!stop.isBefore(start), "stop must be >= start");

		this.stop = stop;
		this.start = start;
		this.duration = duration;

		reset();
	}

	/**
	 * Build a sequence that will return values between {@code start} and {@code end} (inclusive) with an increment of
	 * {@code increment}. For instance, in:
	 * <pre>
	 *     s1 = new DateSequence(
	 *     	new DateTime(1, 1, 1, 0, 0, 0),
	 *     	new DateTime(1, 1, 3, 0, 0, 0),
	 *     	new Period.days(1)
	 *     );
	 *     s1 = new DateSequence(
	 *     	new DateTime(1, 1, 1, 0, 0, 0),
	 *     	new DateTime(1, 1, 5, 0, 0, 0),
	 *     	new Period.days(1)
	 *     );
	 * </pre>
	 * {@code s1} will generate the following dates: {@code 1-1-1T0:0:0}, {@code 1-1-2T0:0:0}, {@code 1-1-3T0:0:0} and
	 * {@code s2} will generate {@code 1-1-1T0:0:0}, {@code 1-1-3T0:0:0}, {@code 1-1-5T0:0:0}.
	 *
	 * @param start first value in sequence, inclusive.
	 * @param stop last element in sequence, inclusive.
	 * @param period increment.
	 *
	 * @throws java.lang.IllegalArgumentException if start is less than stop or if increment is less than or equal to
	 * 0ms.
	 */
	public DateSequence(DateTime start, DateTime stop, ReadablePeriod period) {
		AssertionUtils.assertTrue(period.toPeriod().toStandardDuration().isLongerThan(Duration.ZERO), "period must be > 0ms");
		AssertionUtils.assertTrue(!stop.isBefore(start), "stop must be >= start");

		this.start = start;
		this.stop = stop;
		this.period = period;

		reset();
	}

	/**
	 * Resets the {@code DateSequence}. After this method is called, the following {@link #next} or {@link #current}
	 * call returns the first generated value.
	 */
	@Override
	public void reset() {
		current = null;
		next = start;
	}

	/**
	 * Returns the current sequence value. Implicitly calls {@link #next} if {@code next} was never called before since
	 * creation of last {@link #reset}.
	 * @return the current sequence value
	 */
	@Override
	public Date current() {
		if (null == current) {
			return next();
		}

		return current.toDate();
	}

	/**
	 * Returns {@code true} if the generator has more value to generate (In other words, returns {@code true} if
	 * {@link #next} would return a value rather than throwing an exception.)
	 *
	 * @return {@code true} if the iteration has more value.
	 */
	@Override
	public boolean hasNext() {
		return !next.isAfter(stop);
	}

	/**
	 * Returns the next generated value.
	 *
	 * @return the next generated value.
	 *
	 * @throws java.util.NoSuchElementException if the iteration has no more value.
	 */
	@Override
	public Date next() {
		if (!hasNext()) {
			throw new NoSuchElementException(toString() + " ended");
		}

		current = next;
		computeNext();

		return current();
	}

	private void computeNext() {
		Object durationOrPeriod = getDurationOrPeriod();

		if (durationOrPeriod instanceof ReadableDuration) {
			next = next.plus((ReadableDuration) durationOrPeriod);
		} else {
			next = next.plus((ReadablePeriod) durationOrPeriod);
		}
	}

	@Override
	public String toString() {
		return "DateSequence{" + start + ";" + stop + ";" + getDurationOrPeriod() + "}";
	}

	private Object getDurationOrPeriod() {
		if (null != duration) {
			return duration;
		}

		return period;
	}

}
