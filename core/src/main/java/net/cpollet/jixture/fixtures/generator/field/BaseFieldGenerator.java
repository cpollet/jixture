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

package net.cpollet.jixture.fixtures.generator.field;

/**
 * Base class for {@code FieldGenerator}. Implements {@code remove} method.
 *
 * @author Christophe Pollet
 */
public abstract class BaseFieldGenerator<T> implements FieldGenerator<T> {
	/**
	 * The remove method is not supported by a {@code BaseFieldGenerator}, it thus always throws an
	 * {@code UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException always.
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
