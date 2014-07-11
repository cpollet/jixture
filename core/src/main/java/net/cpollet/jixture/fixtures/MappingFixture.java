/*
 * Copyright 2013 Christophe Pollet
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

import net.cpollet.jixture.fixtures.extraction.ExtractionCapableFixture;
import net.cpollet.jixture.fixtures.extraction.ExtractionResult;
import net.cpollet.jixture.fixtures.extraction.ExtractorDelegate;
import net.cpollet.jixture.fixtures.extraction.ExtractorMatcher;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple fixture that loads a collection of entities.
 *
 * @author Christophe Pollet
 */
public class MappingFixture extends BaseObjectFixture implements ExtractionCapableFixture<MappingFixture> {
	private List<Object> objects;

	private ExtractorDelegate extractorDelegate;
	private boolean extractionDone;

	/**
	 * @param objectsToAdd a list of entities to load into database. The entities can be instances of different mapping
	 *                     classes, targeting different tables. Objects are loaded in order.
	 */
	public MappingFixture(Object... objectsToAdd) {
		objects = new LinkedList<Object>();
		extractorDelegate = new ExtractorDelegate();
		extractionDone = false;

		addObjects(objectsToAdd);
	}

	/**
	 * Add entities to the current list of entities to load.
	 *
	 * @param objectsToAdd the list of entities to add. Objects are loaded in order.
	 * @return the current fixture instance.
	 */
	@Override
	public Fixture addObjects(Object... objectsToAdd) {
		if (0 < objectsToAdd.length) {
			Collections.addAll(objects, objectsToAdd);
		}

		return this;
	}

	/**
	 * Returns all entities that this fixture has to load into database.
	 *
	 * @return the list of entities to load.
	 */
	@Override
	public List<Object> getObjects() {
		return objects;
	}

	/**
	 * Add an extractor matcher.
	 *
	 * @see net.cpollet.jixture.fixtures.extraction.ExtractorMatcher
	 *
	 * @param extractorMatcher the extraction matcher to add.
	 * @return the current instance.
	 */
	@Override
	public MappingFixture addExtractorMatcher(ExtractorMatcher extractorMatcher) {
		extractorDelegate.addExtractorMatcher(extractorMatcher);
		return this;
	}

	/**
	 * Returns the extraction result.
	 * @return the extraction result.
	 */
	@Override
	public ExtractionResult getExtractionResult() {
		extractEntities();
		return extractorDelegate.getExtractionResult();
	}

	private void extractEntities() {
		if (!extractionDone) {
			for (Object object : objects) {
				extractorDelegate.extractEntity(object);
			}

			extractionDone = true;
		}
	}
}
