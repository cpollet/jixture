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

import java.util.NoSuchElementException;

/**
 * Generates {@link java.lang.String} given a format (see {@link String#format(String, Object...)}) and a
 * {@link net.cpollet.jixture.fixtures.generator.field.FieldGenerator} instance.
 *
 * @see String#format(String, Object...)
 *
 * @author Christophe Pollet
 */
public class SimpleStringSequence extends BaseFieldGenerator<String> {
	private String format;
	private String current;
	private FieldGenerator generator;

	/**
	 * Generates a @{code String} based on a format and a generator.
	 *
	 * @param format the format to use. Must be compliant with {@link String#format(String, Object...)}.
	 * @param generator the generator to use. The generated values are be included in the {@code format}
	 */
	public SimpleStringSequence(String format, FieldGenerator generator) {
		this.format = format;
		this.generator = generator;
	}

	/**
	 * Resets the {@code SimpleStringSequence}. After this method is called, the following {@link #next} or
	 * {@link #current} call returns the first generated value.
	 */
	@Override
	public void reset() {
		generator.reset();
	}

	/**
	 * Returns {@code true} if the generator has more value to generate (In other words, returns {@code true} if
	 * {@link #next} would return a value rather than throwing an exception.)
	 *
	 * @return {@code true} if the iteration has more value.
	 */
	@Override
	public boolean hasNext() {
		return generator.hasNext();
	}

	/**
	 * Returns the next generated value.
	 *
	 * @return the next generated value.
	 *
	 * @throws java.util.NoSuchElementException if the iteration has no more value.
	 */
	@Override
	public String next() {
		if (!hasNext()) {
			throw new NoSuchElementException(toString() + " ended");
		}

		current = String.format(format, generator.next());

		return current();
	}

	/**
	 * Returns the current sequence value. Implicitly calls {@link #next} if {@code next} was never called before since
	 * creation of last {@link #reset}.
	 *
	 * @return the current sequence value.
	 */
	@Override
	public String current() {
		if (current == null) {
			return next();
		}

		return current;
	}

	@Override
	public String toString() {
		return "SimpleStringSequence{'" + format + "';" + generator + '}';
	}
}
