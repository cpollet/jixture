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

import java.util.List;

/**
 * Transformable fixture that support entities extraction implement this interface.
 *
 * @see net.cpollet.jixture.fixtures.TransformableFixture
 *
 * @author Christophe Pollet
 */
public interface TransformableExtractionCapableFixture<T> extends ExtractionCapableFixture<T> {
	/**
	 * This method passes the list of transformed mappings to the transformable fixture in order for it to be able to
	 * extract required entities. This method should only be called from a
	 * {@link net.cpollet.jixture.fixtures.transformers.FixtureTransformer}
	 *
	 * @see net.cpollet.jixture.fixtures.TransformableFixture
	 * @see net.cpollet.jixture.fixtures.transformers.FixtureTransformer
	 *
	 * @param objects the transformed mappings.
	 */
	void populateExtractionResult(List<Object> objects);
}
