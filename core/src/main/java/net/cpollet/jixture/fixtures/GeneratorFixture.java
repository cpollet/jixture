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

import net.cpollet.jixture.fixtures.generator.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Christophe Pollet
 */
public class GeneratorFixture implements ScrollableFixture {
	private static final Logger logger = LoggerFactory.getLogger(GeneratorFixture.class);

	private List<Generator> generators;
	private boolean started;

	private Iterator<Generator> generatorIterator;
	private Generator currentGenerator;

	public GeneratorFixture(Generator... generators) {
		this.generators = new LinkedList<Generator>();
		addGenerators(generators);

		this.started = false;
	}

	public GeneratorFixture addGenerators(Generator... generatorsToAdd) {
		assertGeneratorNotStarted();

		if (generatorsToAdd.length > 0) {
			Collections.addAll(generators, generatorsToAdd);
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
		generatorIterator = generators.iterator();

		if (generatorIterator.hasNext()) {
			currentGenerator = generatorIterator.next();
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
		return currentGenerator != null && currentGenerator.hasNext();
	}

	private boolean hasNextGeneratorsObjectsToGenerate() {
		if (generatorIterator.hasNext()) {
			currentGenerator = generatorIterator.next();
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

		Object object = currentGenerator.next();
		logger.debug("Generated {}", object);
		return object;
	}

	@Override
	public List<Class> getClassesToDelete() {
		List<Class> classesToDelete = new ArrayList<Class>(generators.size());

		for (Generator generator : generators) {
			classesToDelete.add(generator.getGeneratedClass());
		}

		return classesToDelete;
	}
}
