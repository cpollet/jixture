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

/**
 * Generates an unbounded sequence of {@link java.lang.Integer}.
 *
 * @author Christophe Pollet
 */
public class IntegerIndexSequence extends BaseFieldGenerator<Integer> {
	private int start;
	private Integer current;
	private int next;

	/**
	 * Build a sequence that will return values from {@code start} with an increment of 1.
	 * <pre>
	 *     s = new IntegerIndexSequence(1);
	 * </pre>
	 * {@code s} will generate the following numbers: 1, 2, 3, 4, ...
	 *
	 * @param start first value in sequence, inclusive.
	 */
	public IntegerIndexSequence(Integer start) {
		this.start = start;
		this.next = start;

		reset();
	}

	/**
	 * Build a sequence that will return values from 0 with an increment of 1.
	 * <pre>
	 *     s = new IntegerIndexSequence();
	 * </pre>
	 * {@code s} will generate the following numbers: 0, 1, 2, 3, ...
	 */
	public IntegerIndexSequence() {
		this(0);
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void reset() {
		// nothing
	}

	/**
	 * Returns the next sequence value.
	 *
	 * @return the next sequence value
	 */
	@Override
	public Integer current() {
		return next();
	}

	/**
	 * Always returns false. This is mandatory to make sure this generator does not force a generator to generate values
	 * forever.
	 *
	 * @return {@code false}.
	 */
	@Override
	public boolean hasNext() {
		return false;
	}

	/**
	 * Returns the next generated value.
	 *
	 * @return the next generated value.
	 */
	@Override
	public Integer next() {
		current = next;
		next += 1;

		return current;
	}

	@Override
	public String toString() {
		return "IntegerIndexSequence{" + start + '}';
	}
}
