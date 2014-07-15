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

import net.cpollet.jixture.fixtures.capacities.extraction.ExtractionCapableFixture;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractionResult;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractorDelegate;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractorMatcher;
import net.cpollet.jixture.fixtures.capacities.filtering.Filter;
import net.cpollet.jixture.io.InputStreamUtils;

import java.io.InputStream;
import java.util.List;

/**
 * Loads fixture from <a href="http://dbunit.sourceforge.net/">DbUnit</a>-like XML files. This fixture is transformed
 * into an {@link net.cpollet.jixture.fixtures.ObjectFixture} using
 * {@link net.cpollet.jixture.fixtures.transformers.XmlFileFixtureTransformer}.
 *
 * @see net.cpollet.jixture.fixtures.ObjectFixture
 * @see net.cpollet.jixture.fixtures.transformers.XmlFileFixtureTransformer
 *
 * @author Christophe Pollet
 */
public class XmlFileFixture implements TransformableFixture, //
		ExtractionCapableFixture {
	private InputStream fileInputStream;

	private ExtractorDelegate extractorDelegate;

	private Filter filter;

	public XmlFileFixture(String filePath) {
		fileInputStream = InputStreamUtils.getInputStream(filePath);
		extractorDelegate = new ExtractorDelegate();
	}

	public InputStream getInputStream() {
		return fileInputStream;
	}

	/**
	 * Add an extractor matcher.
	 *
	 * @see net.cpollet.jixture.fixtures.capacities.extraction.ExtractorMatcher
	 *
	 * @param extractorMatcher the extraction matcher to add.
	 * @return the current instance.
	 */
	public XmlFileFixture addExtractorMatcher(ExtractorMatcher extractorMatcher) {
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
