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
 * Generates an unbounded sequence of {@link java.lang.String}.
 *
 * @author Christophe Pollet
 */
public class StringIndexSequence extends BaseFieldGenerator<String> {
	private final String format;
	private final Long start;
	private Long next;

	/**
	 * Generates a @{code String} based on a format and a ever growing number.
	 *
	 * @param format the format to use. Must be compliant with {@link String#format(String, Object...)}.
	 */
	public StringIndexSequence(String format) {
		this(format, 0L);
	}

	/**
	 * Generates a @{code String} based on a format and a ever growing number.
	 *
	 * @param format the format to use. Must be compliant with {@link String#format(String, Object...)}.
	 * @param start  the start index of the sequence
	 */
	public StringIndexSequence(String format, Long start) {
		this.format = format;
		this.start = start;
		this.next = start;
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
	public String current() {
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
	public String next() {
		Long current = next;
		next += 1;

		return String.format(format, current);
	}

	@Override
	public String toString() {
		return "StringIndexSequence{" + start + '}';
	}
}

