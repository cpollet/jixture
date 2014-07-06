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

import java.util.Iterator;

/**
 * Generates field values.
 *
 * @author Christophe Pollet
 */
public interface FieldGenerator<T> extends Iterator<T> {
	/**
	 * Resets the {@code FieldGenerator}. After this method is called, the following {@link #next} or {@link #current}
	 * call returns the first generated element.
	 */
	void reset();

	/**
	 * Returns the current element. In other words, it returns the same element as the last {@link #next} call.
	 *
	 * @return the current element.
	 */
	T current();
}
