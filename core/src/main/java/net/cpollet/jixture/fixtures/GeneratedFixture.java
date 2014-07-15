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

import net.cpollet.jixture.fixtures.capacities.cleaning.CleanableFixture;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractionCapableFixture;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractionResult;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractorDelegate;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractorMatcher;
import net.cpollet.jixture.fixtures.capacities.filtering.Filter;
import net.cpollet.jixture.fixtures.capacities.filtering.FilterableFixture;
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
 * actual entities generators, the {@code GeneratedFixture} is mainly a mean of aggregating them.
 *
 * @author Christophe Pollet
 * @see net.cpollet.jixture.fixtures.generator.fixture.FixtureGenerator
 */
public class GeneratedFixture implements ScrollableFixture,
		ExtractionCapableFixture, //
		CleanableFixture, //
		FilterableFixture {
	private static final Logger logger = LoggerFactory.getLogger(GeneratedFixture.class);

	private List<FixtureGenerator> fixtureGenerators;
	private boolean started;

	private Iterator<FixtureGenerator> generatorIterator;
	private FixtureGenerator currentFixtureGenerator;

	private ExtractorDelegate extractorDelegate;

	private Filter filter;

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
		this.extractorDelegate = new ExtractorDelegate();

		addGenerators(fixtureGenerators);

		this.started = false;
	}

	public GeneratedFixture addGenerators(FixtureGenerator... generatorsToAdd) {
		assertGeneratorNotStarted();

		if (0 < generatorsToAdd.length) {
			Collections.addAll(fixtureGenerators, generatorsToAdd);
		}

		return this;
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

		return hasCurrentGeneratorMoreObjectsToGenerate() || hasNextGeneratorsObjectsToGenerate();
	}

	private void assertGeneratorStarted() {
		if (!started) {
			throw new IllegalStateException("Generator not started");
		}
	}

	private boolean hasCurrentGeneratorMoreObjectsToGenerate() {
		return null != currentFixtureGenerator && currentFixtureGenerator.hasNext();
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
		extractorDelegate.extractEntity(object);

		return object;
	}

	/**
	 * Returns an ordered iterator of mapping classes to delete from database.
	 *
	 * @return an ordered iterator of mapping classes to delete from database.
	 */
	@Override
	public Iterator<Class> getClassesToDeleteIterator() {
		return getClassesToDelete().descendingIterator();
	}

	private LinkedList<Class> getClassesToDelete() {
		LinkedList<Class> classesToDelete = new LinkedList<Class>();

		for (FixtureGenerator fixtureGenerator : fixtureGenerators) {
			classesToDelete.add(fixtureGenerator.getGeneratedClass());
		}

		return classesToDelete;
	}

	/**
	 * Add an extractor matcher.
	 *
	 * @param extractorMatcher the extraction matcher to add.
	 * @return the current instance.
	 * @see net.cpollet.jixture.fixtures.capacities.extraction.ExtractorMatcher
	 */
	public GeneratedFixture addExtractorMatcher(ExtractorMatcher extractorMatcher) {
		extractorDelegate.addExtractorMatcher(extractorMatcher);
		return this;
	}

	/**
	 * Returns the extraction result.
	 *
	 * @return the extraction result.
	 */
	@Override
	public ExtractionResult getExtractionResult() {
		return extractorDelegate.getExtractionResult();
	}

	public GeneratedFixture setFilter(Filter filter) {
		this.filter = filter;
		return this;
	}

	/**
	 * Determines whether the entity must be inserted in database or not.
	 *
	 * @param entity the entity to filter.
	 * @return {@code true} if the entity must be inserted.
	 */
	@Override
	public boolean filter(Object entity) {
		return null == filter || filter.filter(entity);
	}
}
