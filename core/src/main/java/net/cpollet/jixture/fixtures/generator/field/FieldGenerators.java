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

import org.joda.time.DateTime;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadablePeriod;

import java.util.List;

/**
 * Helper class to create instance of {@link net.cpollet.jixture.fixtures.generator.field.FieldGenerator} in a factory
 * style.
 *
 * @author Christophe Pollet
 */
public abstract class FieldGenerators {
	public static FieldGenerator sequence(DateTime start, DateTime stop) {
		return new DateSequence(start, stop);
	}

	public static FieldGenerator sequence(DateTime start, DateTime stop, ReadableDuration duration) {
		return new DateSequence(start, stop, duration);
	}

	public static FieldGenerator sequence(DateTime start, DateTime stop, ReadablePeriod period) {
		return new DateSequence(start, stop, period);
	}

	public static FieldGenerator sequence(Integer start, Integer stop) {
		return new IntegerSequence(start, stop);
	}

	public static FieldGenerator sequence(Integer start, Integer stop, Integer increment) {
		return new IntegerSequence(start, stop, increment);
	}

	public static FieldGenerator sequence(Long start, Long stop) {
		return new LongSequence(start, stop);
	}

	public static FieldGenerator sequence(Long start, Long stop, Long increment) {
		return new LongSequence(start, stop, increment);
	}

	public static FieldGenerator sequence(Integer start) {
		return new IntegerIndexSequence(start);
	}

	public static FieldGenerator sequence(Long start) {
		return new LongIndexSequence(start);
	}

	public static FieldGenerator sequence(String format) {
		return new StringIndexSequence(format);
	}

	public static FieldGenerator sequence(String format, Long start) {
		return new StringIndexSequence(format, start);
	}

	public static <T> FieldGenerator in(List<T> values) {
		return new ListSequence<T>(values);
	}

	public static <T> FieldGenerator in(T... values) {
		return new ListSequence<T>(values);
	}

	public static FieldGenerator in(String format, FieldGenerator generator) {
		return new SimpleStringSequence(format, generator);
	}

	public static MultiFieldGenerator multiField() {
		return new MultiFieldGenerator();
	}

}
