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

package net.cpollet.jixture.fixtures.extraction;


/**
 * Fixtures that support entities extraction implement this interface.
 *
 * @author Christophe Pollet
 */
public interface ExtractionCapableFixture<T extends ExtractionCapableFixture> {
	/**
	 * Add an extractor matcher.
	 *
	 * @see net.cpollet.jixture.fixtures.extraction.ExtractorMatcher
	 *
	 * @param extractorMatcher the extraction matcher to add.
	 * @return the current instance.
	 */
	T addExtractorMatcher(ExtractorMatcher extractorMatcher);

	/**
	 * Returns the extraction result.
	 * @return the extraction result.
	 */
	ExtractionResult getExtractionResult();
}
