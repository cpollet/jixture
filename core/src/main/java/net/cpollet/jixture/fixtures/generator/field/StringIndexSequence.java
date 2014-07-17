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
 * @author Christophe Pollet
 */
public class StringIndexSequence extends BaseFieldGenerator<String> {
	private final String format;
	private final Long start;
	private Long next;

	public StringIndexSequence(String format) {
		this(format, 0L);
	}

	public StringIndexSequence(String format, Long start) {
		this.format = format;
		this.start = start;
		this.next = start;
	}

	@Override
	public void reset() {
		// nothing
	}

	@Override
	public String current() {
		return next();
	}

	@Override
	public boolean hasNext() {
		return false;
	}

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

