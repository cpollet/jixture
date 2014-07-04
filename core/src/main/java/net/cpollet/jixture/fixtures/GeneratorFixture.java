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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Christophe Pollet
 */
public class GeneratorFixture implements ScrollableFixture {
	private static final Logger logger = LoggerFactory.getLogger(GeneratorFixture.class);

	private List<FixtureGenerator> fixtureGenerators;
	private boolean started;

	private Iterator<FixtureGenerator> generatorIterator;
	private FixtureGenerator currentFixtureGenerator;

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

	public void start() {
		assertGeneratorNotStarted();

		started = true;

		generatorIterator = fixtureGenerators.iterator();

		if (generatorIterator.hasNext()) {
			currentFixtureGenerator = generatorIterator.next();
		}
	}

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

	@Override
	public Object next() {
		assertGeneratorStarted();

		if (!hasNext()) {
			throw new IllegalStateException("No object to generate");
		}

		Object object = currentFixtureGenerator.next();
		logger.debug("Generated {}", object);
		return object;
	}

	@Override
	public List<Class> getClassesToDelete() {
		List<Class> classesToDelete = new ArrayList<Class>(fixtureGenerators.size());

		for (FixtureGenerator fixtureGenerator : fixtureGenerators) {
			classesToDelete.add(fixtureGenerator.getGeneratedClass());
		}

		return classesToDelete;
	}
}
