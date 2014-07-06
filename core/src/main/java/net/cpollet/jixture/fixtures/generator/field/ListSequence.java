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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Generates a sequence of values based on a predefined list.
 *
 * @author Christophe Pollet
 */
public class ListSequence<T> extends BaseFieldGenerator<T> {
	private List<T> values;
	private Iterator<T> iterator;
	private T current;

	/**
	 * @param values the list of values.
	 */
	public ListSequence(List<T> values) {
		this.values = values;

		reset();
	}

	/**
	 * @param values the values.
	 */
	public ListSequence(T... values) {
		this(Arrays.asList(values));
	}

	/**
	 * Resets the {@code ListSequence}. After this method is called, the following {@link #next} or {@link #current}
	 * call returns the first generated value.
	 */
	@Override
	public void reset() {
		iterator = values.iterator();
		current = null;
	}

	/**
	 * Returns the current sequence value. Implicitly calls {@link #next} if {@code next} was never called before since
	 * creation of last {@link #reset}.
	 *
	 * @return the current sequence value
	 */
	@Override
	public T current() {
		if (null == current) {
			return next();
		}

		return current;
	}

	/**
	 * Returns {@code true} if the list has more elements (In other words, returns {@code true} if {@link #next} would
	 * return a value rather than throwing an exception.)
	 *
	 * @return {@code true} if the iteration has more value.
	 */
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	/**
	 * Returns the next value in list.
	 *
	 * @return the next value in list.
	 *
	 * @throws java.util.NoSuchElementException if the iteration has no more value.
	 */
	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException(toString() + " ended");
		}

		current = iterator.next();

		return current();
	}

	@Override
	public String toString() {
		return "ListSequence{" + values + '}';
	}
}
