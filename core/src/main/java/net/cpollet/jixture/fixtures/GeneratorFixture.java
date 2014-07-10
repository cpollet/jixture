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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Generate entities on the fly. It is backed by an instances of
 * {@link net.cpollet.jixture.fixtures.generator.fixture.FixtureGenerator}. Those {@code FixtureGenerators} are the
 * actual entities generators, the {@code GeneratorFixture} is mainly a mean of aggregating them.
 *
 * @see net.cpollet.jixture.fixtures.generator.fixture.FixtureGenerator
 *
 * @author Christophe Pollet
 */
public class GeneratorFixture extends BaseScrollableFixture {
	private static final Logger logger = LoggerFactory.getLogger(GeneratorFixture.class);

	private List<FixtureGenerator> fixtureGenerators;
	private boolean started;

	private Iterator<FixtureGenerator> generatorIterator;
	private FixtureGenerator currentFixtureGenerator;

	public static TemplateGenerator template(Object templateObject) {
		return new TemplateGenerator(templateObject);
	}

	public static SimpleGenerator simple(Class generatedClass, int quantity) {
		return new SimpleGenerator(generatedClass, quantity);
	}

	/**
	 * @param fixtureGenerators the generators to use to generate the entities ot load.
	 */
	public GeneratorFixture(FixtureGenerator... fixtureGenerators) {
		this.fixtureGenerators = new LinkedList<FixtureGenerator>();
		addGenerators(fixtureGenerators);

		this.started = false;
	}

	public GeneratorFixture addGenerators(FixtureGenerator... generatorsToAdd) {
		assertGeneratorNotStarted();

		if (generatorsToAdd.length > 0) {
			Collections.addAll(fixtureGenerators, generatorsToAdd);
		}

		return this;
	}

	private void assertGeneratorNotStarted() {
		if (started) {
			throw new IllegalStateException("Generator already started");
		}
	}

	public GeneratorFixture start() {
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
	 *
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
		return object;
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
}
