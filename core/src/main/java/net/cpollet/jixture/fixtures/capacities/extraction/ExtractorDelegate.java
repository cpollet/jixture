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

package net.cpollet.jixture.fixtures.capacities.extraction;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Christophe Pollet
 */
public class ExtractorDelegate {
	private List<ExtractorMatcher> extractorMatchers;
	private ExtractionResult extractionResult;

	public ExtractorDelegate() {
		extractorMatchers = new LinkedList<ExtractorMatcher>();
		extractionResult = new ExtractionResult();
	}

	public void extractEntity(Object entity) {
		for (ExtractorMatcher extractorMatcher : extractorMatchers) {
			extract(extractorMatcher, entity);
		}
	}

	private void extract(ExtractorMatcher extractorMatcher, Object entity) {
		if (extractorMatcher.getMatcher().matches(entity)) {
			extractionResult.add(entity, extractorMatcher.getName());
		}
	}

	public void addExtractorMatcher(ExtractorMatcher matcher) {
		this.extractorMatchers.add(matcher);
	}

	public ExtractionResult getExtractionResult() {
		return extractionResult;
	}
}
