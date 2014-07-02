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
 * @author Christophe Pollet
 */
public class Sequence implements FieldGenerator {
	private int start;
	private int increment;
	private int stop;
	private int current;
	private int next;

	public Sequence(int start, int stop, int increment) {
		Assert.isTrue(stop >= start, "stop must be >= start");
		Assert.isTrue(0 < increment, "increment must be > 0");


		this.start = start;
		this.increment = increment;
		this.stop = stop;

		reset();
	}

	public Sequence(int start, int stop) {
		this(start, stop, 1);
	}

	@Override
	public void reset() {
		next = start;
	}

	@Override
	public boolean hasNext() {
		return next <= stop;
	}

	@Override
	public Object next() {
		if (!hasNext()) {
			throw new NoSuchElementException("Sequence ended");
		}

		current = next;
		next = next + increment;

		return current();
	}

	@Override
	public Object current() {
		return current;
	}
}
