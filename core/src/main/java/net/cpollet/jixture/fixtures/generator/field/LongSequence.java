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

import org.springframework.util.Assert;

import java.util.NoSuchElementException;

/**
 * Generates a sequence of {@link java.lang.Long}.
 *
 * @author Christophe Pollet
 */
public class LongSequence extends BaseFieldGenerator {
	private long start;
	private long increment;
	private long stop;
	private Long current;
	private long next;

	/**
	 * Build a sequence that will return values between {@code start} and {@code end} (inclusive) with an increment of
	 * {@code increment}. For instance, in:
	 * <pre>
	 *     s1 = new LongSequence(1l, 10l, 2l);
	 *     s2 = new LongSequence(1l, 10l, 1l);
	 * </pre>
	 * {@code s1} will generate the following numbers: 1l, 3l, 5l, 7l, 9l and {@code s2} will generate 1l, 2l, 3l, 4l,
	 * 5l, 6l, 7l, 8l, 9l, 10l.
	 *
	 * @param start first value in sequence, inclusive.
	 * @param stop last element in sequence, inclusive.
	 * @param increment increment.
	 *
	 * @throws java.lang.IllegalArgumentException if start is less than stop or if increment is less than or equal to 0.
	 */
	public LongSequence(long start, long stop, long increment) {
		Assert.isTrue(stop >= start, "stop must be >= start");
		Assert.isTrue(0 < increment, "increment must be > 0");

		this.start = start;
		this.increment = increment;
		this.stop = stop;

		reset();
	}

	/**
	 * Build a sequence that will return values between {@code start} and {@code end} (inclusive) with an increment of
	 * 1l.
	 *
	 * @see #LongSequence(long, long, long)
	 *
	 * @param start first value in sequence, inclusive.
	 * @param stop last element in sequence, inclusive.
	 */
	public LongSequence(long start, long stop) {
		this(start, stop, 1);
	}

	/**
	 * Resets the {@code LongGenerator}. After this method is called, the following {@link #next} or {@link #current}
	 * call returns the first generated value.
	 */
	@Override
	public void reset() {
		current = null;
		next = start;
	}

	/**
	 * Returns {@code true} if the generator has more value to generate (In other words, returns {@code true} if
	 * {@link #next} would return a value rather than throwing an exception.)
	 *
	 * @return {@code true} if the iteration has more value.
	 */
	@Override
	public boolean hasNext() {
		return next <= stop;
	}

	/**
	 * Returns the next generated value.
	 *
	 * @return the next generated value.
	 *
	 * @throws java.util.NoSuchElementException if the iteration has no more value.
	 */
	@Override
	public Object next() {
		if (!hasNext()) {
			throw new NoSuchElementException(toString() + " ended");
		}

		current = next;
		next = next + increment;

		return current();
	}

	/**
	 * Returns the current sequence value. Implicitly calls {@link #next} if {@code next} was never called before since
	 * creation of last {@link #reset}.
	 *
	 * @return the current sequence value.
	 */
	@Override
	public Object current() {
		if (null == current) {
			return next();
		}

		return current;
	}

	@Override
	public String toString() {
		return "LongSequence{" + start + ';' + increment + ';' +  stop + '}';
	}
}
