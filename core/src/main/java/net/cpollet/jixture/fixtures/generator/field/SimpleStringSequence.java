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
 * @author Christophe Pollet
 */
public class SimpleStringSequence implements FieldGenerator {
	private String format;
	private String current;
	private FieldGenerator generator;

	public SimpleStringSequence(String format, FieldGenerator generator) {
		this.format = format;
		this.generator = generator;
	}

	@Override
	public void reset() {
		generator.reset();
	}

	@Override
	public boolean hasNext() {
		return generator.hasNext();
	}

	@Override
	public Object next() {
		if (!hasNext()) {
			throw new NoSuchElementException(toString() + " ended");
		}

		current = String.format(format, generator.next());

		return current();
	}

	@Override
	public Object current() {
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
