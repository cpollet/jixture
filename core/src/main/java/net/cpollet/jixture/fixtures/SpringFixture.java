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
public class SpringFixture implements TransformableFixture {
	String context;
	List<Class<?>> classes;

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
}
