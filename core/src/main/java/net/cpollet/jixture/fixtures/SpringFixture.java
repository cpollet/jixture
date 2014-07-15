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
import net.cpollet.jixture.fixtures.filter.Filter;

import java.util.Arrays;
import java.util.List;

/**
 * Loads the entities from a Spring context. This fixture is transformed into an
 * {@link net.cpollet.jixture.fixtures.ObjectFixture} using
 * {@link net.cpollet.jixture.fixtures.transformers.SpringFixtureTransformer}.
 *
 * @see net.cpollet.jixture.fixtures.ObjectFixture
 * @see net.cpollet.jixture.fixtures.transformers.SpringFixtureTransformer
 *
 * @author Christophe Pollet
 */
public class SpringFixture implements TransformableFixture, //
		ExtractionCapableFixture<SpringFixture> {
	private String context;
	private List<Class<?>> classes;

	private ExtractorDelegate extractorDelegate;

	private Filter filter;

	/**
	 * @param context the path to the XML Spring file containing the entities beans to load. The
	 *                {@link net.cpollet.jixture.fixtures.transformers.SpringFixtureTransformer} expects the path to be
	 *                relative to {@code classpath:/}.
	 * @param classes the classes of the beans to load. A Spring XML context file could define beans of type {@code A}
	 *                and {@code B} but the fixture could be interested to getting only beans of type {@code B}. In that
	 *                case, pass {@code B} as the list of classes to load. The beans are loaded in order.
	 */
	public SpringFixture(String context, Class<?>... classes) {
		this.context = context;
		this.classes = Arrays.asList(classes);
		setupExtractionDelegate();
	}

	private void setupExtractionDelegate() {
		extractorDelegate = new ExtractorDelegate();
	}

	/**
	 * @param context the path to the XML Spring file containing the entities beans to load. The
	 *                {@link net.cpollet.jixture.fixtures.transformers.SpringFixtureTransformer} expects the path to be
	 *                relative to {@code classpath:/}.
	 * @param classes the classes of the beans to load. A Spring XML context file could define beans of type {@code A}
	 *                and {@code B} but the fixture could be interested to getting only beans of type {@code B}. In that
	 *                case, pass {@code B} as the list of classes to load. The beans are loaded in order.
	 */
	public SpringFixture(String context, List<Class<?>> classes) {
		this.context = context;
		this.classes = classes;
		setupExtractionDelegate();
	}

	/**
	 * Returns the context file path.
	 *
	 * @return the context file path.
	 */
	public String getContext() {
		return context;
	}

	/**
	 * Returns the bean classes to load.
	 *
	 * @return the bean classes to load.
	 */
	public List<Class<?>> getClasses() {
		return classes;
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
	public SpringFixture addExtractorMatcher(ExtractorMatcher extractorMatcher) {
		extractorDelegate.addExtractorMatcher(extractorMatcher);
		return this;
	}

	/**
	 * Returns the extraction result.
	 * @return the extraction result.
	 */
	@Override
	public ExtractionResult getExtractionResult() {
		return extractorDelegate.getExtractionResult();
	}

	/**
	 * This method filter the list of transformed mappings to the transformable fixture in order for it to be able to
	 * extract required entities. This method should only be called from a
	 * {@link net.cpollet.jixture.fixtures.transformers.FixtureTransformer}
	 *
	 * @see net.cpollet.jixture.fixtures.TransformableFixture
	 * @see net.cpollet.jixture.fixtures.transformers.FixtureTransformer
	 *
	 * @param objects the transformed mappings.
	 */
	public void populateExtractionResult(List<Object> objects) {
		for (Object object : objects) {
			extractorDelegate.extractEntity(object);
		}
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
}
