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

import org.hamcrest.Matcher;

/**
 * @author Christophe Pollet
 */
public class ExtractorMatcher {
	private Matcher matcher;
	private String name;

	/**
	 * Create an extractor matcher with a matcher instance. The collection's name is set to '*'
	 *
	 * @param matcher the matcher to use to build the entities collection.
	 * @return the newly created {@code ExtractorMatcher}
	 */
	public static ExtractorMatcher create(Matcher matcher) {
		return new ExtractorMatcher("*", matcher);
	}

	/**
	 * Create an extractor matcher with a given name and a matcher instance.
	 *
	 * @param name the collection's name
	 * @param matcher the matcher to use to build the entities collection.
	 * @return the newly created {@code ExtractorMatcher}
	 */
	public static ExtractorMatcher create(String name, Matcher matcher) {
		return new ExtractorMatcher(name, matcher);
	}

	private ExtractorMatcher(String name, Matcher matcher) {
		this.name = name;
		this.matcher = matcher;
	}

	public Matcher getMatcher() {
		return matcher;
	}

	public String getName() {
		return name;
	}
}
