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
public class LongSequence implements FieldGenerator {
	private long start;
	private long increment;
	private long stop;
	private Long current;
	private long next;

	public LongSequence(long start, long stop, long increment) {
		Assert.isTrue(stop >= start, "stop must be >= start");
		Assert.isTrue(0 < increment, "increment must be > 0");

		this.start = start;
		this.increment = increment;
		this.stop = stop;

		reset();
	}

	public LongSequence(long start, long stop) {
		this(start, stop, 1);
	}

	@Override
	public void reset() {
		current = null;
		next = start;
	}

	@Override
	public boolean hasNext() {
		return next <= stop;
	}

	@Override
	public Object next() {
		if (!hasNext()) {
			throw new NoSuchElementException("LongSequence [" + start + ";" + increment + ";" + stop + "] ended");
		}

		current = next;
		next = next + increment;

		return current();
	}

	/**
	 * Returns the current sequence value. Implicitly calls next() if next() was never called before since creation of
	 * last reset().
	 * @return
	 */
	@Override
	public Object current() {
		if (null == current) {
			return next();
		}

		return current;
	}
}
