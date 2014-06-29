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

package net.cpollet.jixture.fixtures.generator;

/**
 * @author Christophe Pollet
 */
public class SimpleGenerator implements Generator {
	private Class generatedClass;
	private int quantity;

	public SimpleGenerator(Class generatedClass, int quantity) {
		this.generatedClass = generatedClass;
		this.quantity = quantity;
	}

	@Override
	public boolean hasNext() {
		return quantity > 0;
	}

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

	@Override
	public Class getGeneratedClass() {
		return generatedClass;
	}
}
