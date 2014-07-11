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

package net.cpollet.jixture.fixtures;

import net.cpollet.jixture.fixtures.generator.fixture.FixtureGenerator;
import org.hamcrest.Matcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Same as {@link net.cpollet.jixture.fixtures.GeneratedFixture} with extraction of generated entities capabilities. The
 * beans are extracted based on hamcrest matchers.
 *
 * @author Christophe Pollet
 */
public class ExtractableGeneratedFixture extends GeneratedFixture {
	private Matcher extractionMatcher;
	private ExtractionResult extractionResult;

	public ExtractableGeneratedFixture(FixtureGenerator... fixtureGenerators) {
		super(fixtureGenerators);
	}

	public ExtractableGeneratedFixture extractEntities(Matcher matcher) {
		return extractEntities(matcher, new ExtractionResult());
	}

	public ExtractableGeneratedFixture extractEntities(Matcher matcher, ExtractionResult extractionResult) {
		this.extractionMatcher = matcher;
		this.extractionResult = extractionResult;
		return this;
	}

	public ExtractionResult getExtractionResult() {
		return extractionResult;
	}

	@Override
	public ExtractableGeneratedFixture addGenerators(FixtureGenerator... generatorsToAdd) {
		super.addGenerators(generatorsToAdd);
		return this;
	}

	@Override
	public ExtractableGeneratedFixture start() {
		super.start();
		return this;
	}

	@Override
	public Object next() {
		Object object = super.next();

		extractEntity(object);

		return object;
	}

	private void extractEntity(Object entity) {
		if (null != extractionMatcher && extractionMatcher.matches(entity)) {
			extractionResult.add(entity);
		}
	}

	@SuppressWarnings({"NullableProblems", "EqualsWhichDoesntCheckParameterClass", "SuspiciousToArrayCall"})
	public static class ExtractionResult implements List<Object> {
		private List<Object> entities;

		public ExtractionResult() {
			this.entities = new LinkedList<Object>();
		}

		@Override
		public int size() {
			return entities.size();
		}

		@Override
		public boolean isEmpty() {
			return entities.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			return entities.contains(o);
		}

		@Override
		public Iterator<Object> iterator() {
			return entities.iterator();
		}

		@Override
		public Object[] toArray() {
			return entities.toArray();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return entities.toArray(a);
		}

		@Override
		public boolean add(Object o) {
			return entities.add(o);
		}

		@Override
		public boolean remove(Object o) {
			return entities.remove(o);
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return entities.containsAll(c);
		}

		@Override
		public boolean addAll(Collection<?> c) {
			return entities.addAll(c);
		}

		@Override
		public boolean addAll(int index, Collection<?> c) {
			return entities.addAll(index, c);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return entities.removeAll(c);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return entities.retainAll(c);
		}

		@Override
		public void clear() {
			entities.clear();
		}

		@Override
		public boolean equals(Object o) {
			return entities.equals(o);
		}

		@Override
		public int hashCode() {
			return entities.hashCode();
		}

		@Override
		public Object get(int index) {
			return entities.get(index);
		}

		@Override
		public Object set(int index, Object element) {
			return entities.set(index, element);
		}

		@Override
		public void add(int index, Object element) {
			entities.add(index, element);
		}

		@Override
		public Object remove(int index) {
			return entities.remove(index);
		}

		@Override
		public int indexOf(Object o) {
			return entities.indexOf(o);
		}

		@Override
		public int lastIndexOf(Object o) {
			return entities.lastIndexOf(o);
		}

		@Override
		public ListIterator<Object> listIterator() {
			return entities.listIterator();
		}

		@Override
		public ListIterator<Object> listIterator(int index) {
			return entities.listIterator(index);
		}

		@Override
		public List<Object> subList(int fromIndex, int toIndex) {
			return entities.subList(fromIndex, toIndex);
		}
	}
}
