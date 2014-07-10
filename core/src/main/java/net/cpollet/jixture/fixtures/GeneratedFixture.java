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
import net.cpollet.jixture.fixtures.generator.fixture.SimpleGenerator;
import net.cpollet.jixture.fixtures.generator.fixture.TemplateGenerator;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Generate entities on the fly. It is backed by an instances of
 * {@link net.cpollet.jixture.fixtures.generator.fixture.FixtureGenerator}. Those {@code FixtureGenerators} are the
 * actual entities generators, the {@code GeneratedFixture} is mainly a mean of aggregating them.
 *
 * @author Christophe Pollet
 * @see net.cpollet.jixture.fixtures.generator.fixture.FixtureGenerator
 */
public class GeneratedFixture extends BaseScrollableFixture {
	private static final Logger logger = LoggerFactory.getLogger(GeneratedFixture.class);

	private List<FixtureGenerator> fixtureGenerators;
	private boolean started;

	private Iterator<FixtureGenerator> generatorIterator;
	private FixtureGenerator currentFixtureGenerator;

	private Matcher extractionMatcher;
	private ExtractionResult extractionResult;

	public static TemplateGenerator from(Object templateObject) {
		return new TemplateGenerator(templateObject);
	}

	public static SimpleGenerator from(Class generatedClass, int quantity) {
		return new SimpleGenerator(generatedClass, quantity);
	}

	/**
	 * @param fixtureGenerators the generators to use to generate the entities ot load.
	 */
	public GeneratedFixture(FixtureGenerator... fixtureGenerators) {
		this.fixtureGenerators = new LinkedList<FixtureGenerator>();
		addGenerators(fixtureGenerators);

		this.started = false;
	}

	public GeneratedFixture addGenerators(FixtureGenerator... generatorsToAdd) {
		assertGeneratorNotStarted();

		if (generatorsToAdd.length > 0) {
			Collections.addAll(fixtureGenerators, generatorsToAdd);
		}

		return this;
	}

	public GeneratedFixture extractEntities(Matcher matcher) {
		return extractEntities(matcher, new ExtractionResult());
	}

	public GeneratedFixture extractEntities(Matcher matcher, ExtractionResult extractionResult) {
		this.extractionMatcher = matcher;
		this.extractionResult = extractionResult;
		return this;
	}

	public ExtractionResult getExtractionResult() {
		return extractionResult;
	}

	private void assertGeneratorNotStarted() {
		if (started) {
			throw new IllegalStateException("Generator already started");
		}
	}

	public GeneratedFixture start() {
		assertGeneratorNotStarted();

		started = true;

		generatorIterator = fixtureGenerators.iterator();

		if (generatorIterator.hasNext()) {
			currentFixtureGenerator = generatorIterator.next();
		}

		return this;
	}

	/**
	 * Returns {@code true} if the generator has more entity to generate (In other words, returns {@code true} if
	 * {@link #next} would return an entity rather than throwing an exception.)
	 *
	 * @return {@code true} if the iteration has more elements.
	 */
	@Override
	public boolean hasNext() {
		assertGeneratorStarted();

		if (hasCurrentGeneratorMoreObjectsToGenerate()) {
			return true;
		}

		return hasNextGeneratorsObjectsToGenerate();
	}

	private void assertGeneratorStarted() {
		if (!started) {
			throw new IllegalStateException("Generator not started");
		}
	}

	private boolean hasCurrentGeneratorMoreObjectsToGenerate() {
		return currentFixtureGenerator != null && currentFixtureGenerator.hasNext();
	}

	private boolean hasNextGeneratorsObjectsToGenerate() {
		if (generatorIterator.hasNext()) {
			currentFixtureGenerator = generatorIterator.next();
			return hasNext();
		}

		return false;
	}

	/**
	 * Returns the next entity to load.
	 *
	 * @return the next entity to load.
	 * @throws java.util.NoSuchElementException if the iteration has no more elements.
	 */
	@Override
	public Object next() {
		assertGeneratorStarted();

		if (!hasNext()) {
			throw new NoSuchElementException("No more object to generate");
		}

		Object object = currentFixtureGenerator.next();
		logger.debug("Generated {}", object);

		extractEntity(object);

		return object;
	}

	private void extractEntity(Object entity) {
		if (null != extractionMatcher  && extractionMatcher.matches(entity)) {
			extractionResult.add(entity);
		}
	}

	/**
	 * Returns the list of mapping classes representing the tables to truncate.
	 *
	 * @return the list of mapping classes representing the tables to truncate.
	 */
	@Override
	public LinkedList<Class> getClassesToDelete() {
		LinkedList<Class> classesToDelete = new LinkedList<Class>();

		for (FixtureGenerator fixtureGenerator : fixtureGenerators) {
			classesToDelete.add(fixtureGenerator.getGeneratedClass());
		}

		return classesToDelete;
	}

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
