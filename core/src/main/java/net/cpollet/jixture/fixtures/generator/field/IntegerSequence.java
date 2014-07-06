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
 * Generates a sequence of {@link java.lang.Integer}.
 *
 * @author Christophe Pollet
 */
public class IntegerSequence extends BaseFieldGenerator<Integer> {
	private int start;
	private int increment;
	private int stop;
	private Integer current;
	private int next;

	/**
	 * Build a sequence that will return values between {@code start} and {@code end} (inclusive) with an increment of
	 * {@code increment}. For instance, in:
	 * <pre>
	 *     s1 = new IntegerSequence(1, 10, 2);
	 *     s2 = new IntegerSequence(1, 10, 1);
	 * </pre>
	 * {@code s1} will generate the following numbers: 1, 3, 5, 7, 9 and {@code s2} will generate 1, 2, 3, 4, 5, 6, 7,
	 * 8, 9, 10.
	 *
	 * @param start first value in sequence, inclusive.
	 * @param stop last element in sequence, inclusive.
	 * @param increment increment.
	 *
	 * @throws java.lang.IllegalArgumentException if start is less than stop or if increment is less than or equal to 0.
	 */
	public IntegerSequence(int start, int stop, int increment) {
		Assert.isTrue(stop >= start, "stop must be >= start");
		Assert.isTrue(0 < increment, "increment must be > 0");

		this.start = start;
		this.increment = increment;
		this.stop = stop;

		reset();
	}

	/**
	 * Build a sequence that will return values between {@code start} and {@code end} (inclusive) with an increment of
	 * 1.
	 *
	 * @see #IntegerSequence(int, int, int)
	 *
	 * @param start first value in sequence, inclusive.
	 * @param stop last element in sequence, inclusive.
	 */
	public IntegerSequence(int start, int stop) {
		this(start, stop, 1);
	}

	/**
	 * Resets the {@code IntegerGenerator}. After this method is called, the following {@link #next} or {@link #current}
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
	public Integer next() {
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
	 * @return the current sequence value
	 */
	@Override
	public Integer current() {
		if (null == current) {
			return next();
		}

		return current;
	}

	@Override
	public String toString() {
		return "IntegerSequence{" + start + ';' + increment + ';' +  stop + '}';
	}
}
