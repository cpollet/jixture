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

package net.cpollet.jixture.fixtures.capacities.filtering;

/**
 * Helper class to create {@link net.cpollet.jixture.fixtures.capacities.filtering.Filters} instances.
 *
 * @author Christophe Pollet
 */
public abstract class Filters {
	public static Not not(Filter filter) {
		return new Not(filter);
	}

	public static Or or(Filter... filters) {
		return new Or(filters);
	}

	public static And and(Filter... filters) {
		return new And(filters);
	}
}
