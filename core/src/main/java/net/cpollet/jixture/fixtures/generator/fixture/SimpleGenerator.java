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

package net.cpollet.jixture.fixtures.generator.fixture;

/**
 * Returns a predefined amount of instances of a given class.
 *
 * @author Christophe Pollet
 */
public class SimpleGenerator extends BaseFixtureGenerator {
	private Class generatedClass;
	private int quantity;

	/**
	 * @param generatedClass he class to instantiate to create the entity.
	 * @param quantity the amount of entities to return.
	 */
	public SimpleGenerator(Class generatedClass, int quantity) {
		this.generatedClass = generatedClass;
		this.quantity = quantity;
	}

	/**
	 * Returns {@code true} if the generator has more entity to generate (In other words, returns {@code true} if
	 * {@link #next} would return an entity rather than throwing an exception.)
	 *
	 * @return {@code true} if the iteration has more elements.
	 */
	@Override
	public boolean hasNext() {
		return quantity > 0;
	}

	/**
	 * Returns the next generated entity.
	 *
	 * @return the next generated entity.
	 *
	 * @throws java.util.NoSuchElementException if the iteration has no more elements.
	 */
	@Override
	public Object next() {
		quantity--;

		try {
			return generatedClass.newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the class of generated entities.
	 *
	 * @return the class of generated entities.
	 */
	@Override
	public Class getGeneratedClass() {
		return generatedClass;
	}
}
