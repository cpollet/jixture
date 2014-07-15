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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Base class for {@link ObjectFixture} implementations. Provide sensible implementation of {@link #getClassesToDelete}.
 *
 * @author Christophe Pollet
 */
public abstract class BaseObjectFixture<T extends BaseObjectFixture> implements ObjectFixture<Object>, //
		ExtractionCapableFixture<T>, //
		CleanableFixture, //
		FilterableFixture {

	private Iterator<Object> iterator;

	private ExtractorDelegate extractorDelegate;
	private boolean extractionDone;

	private Filter filter;

	protected BaseObjectFixture() {
		extractorDelegate = new ExtractorDelegate();
		extractionDone = false;
	}

	/**
	 * Returns the list of mapping classes representing the tables to truncate.
	 *
	 * @return the list of mapping classes representing the tables to truncate.
	 */
	private LinkedList<Class> getClassesToDelete() {
		LinkedHashSet<Class> classesToDelete = new LinkedHashSet<Class>();

		for (Object object : getObjects()) {
			classesToDelete.add(object.getClass());
		}

		return new LinkedList<Class>(classesToDelete);
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

	@Override
	public boolean hasNext() {
		return iterator().hasNext();
	}

	@Override
	public Object next() {
		return iterator().next();
	}

	private Iterator<Object> iterator() {
		if (null == iterator) {
			iterator = getObjects().iterator();
		}

		return iterator;
	}

	/**
	 * Add an extractor matcher.
	 *
	 * @param extractorMatcher the extraction matcher to add.
	 * @return the current instance.
	 * @see net.cpollet.jixture.fixtures.capacities.extraction.ExtractorMatcher
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T addExtractorMatcher(ExtractorMatcher extractorMatcher) {
		extractorDelegate.addExtractorMatcher(extractorMatcher);
		return (T) this;
	}

	/**
	 * Returns the extraction result.
	 *
	 * @return the extraction result.
	 */
	@Override
	public ExtractionResult getExtractionResult() {
		extractEntities();
		return extractorDelegate.getExtractionResult();
	}

	private void extractEntities() {
		if (!extractionDone) {
			for (Object object : getObjects()) {
				extractorDelegate.extractEntity(object);
			}

			extractionDone = true;
		}
	}

	@SuppressWarnings("unchecked")
	public T setFilter(Filter filter) {
		this.filter = filter;
		return (T) this;
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
