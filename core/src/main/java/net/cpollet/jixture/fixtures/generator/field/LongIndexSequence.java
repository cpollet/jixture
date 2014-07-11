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
 * Generates an unbounded sequence of {@link java.lang.Long}.
 *
 * @author Christophe Pollet
 */
public class LongIndexSequence extends BaseFieldGenerator<Long> {
	private long start;
	private long next;

	/**
	 * Build a sequence that will return values from {@code start} with an increment of 1l.
	 * <pre>
	 *     s = new LongIndexSequence(1);
	 * </pre>
	 * {@code s} will generate the following numbers: 1l, 2l, 3l, 4l, ...
	 *
	 * @param start     first value in sequence, inclusive.
	 */
	public LongIndexSequence(Long start) {
		this.start = start;
		this.next = start;

		reset();
	}

	/**
	 * Build a sequence that will return values from 0l with an increment of 1l.
	 * <pre>
	 *     s = new LongIndexSequence();
	 * </pre>
	 * {@code s} will generate the following numbers: 0l, 1l, 2l, 3l, ...
	 */
	public LongIndexSequence() {
		this(0l);
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
	public Long current() {
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
	public Long next() {
		Long current = next;
		next += 1;

		return current;
	}

	@Override
	public String toString() {
		return "LongIndexSequence{" + start + '}';
	}
}
