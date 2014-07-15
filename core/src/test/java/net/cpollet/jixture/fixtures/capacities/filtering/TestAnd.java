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

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestAnd {
	@Test
	public void filter() {
		assertThat(buildAnd(true, true).filter("")).isTrue();
		assertThat(buildAnd(true, false).filter("")).isFalse();
		assertThat(buildAnd(false, true).filter("")).isFalse();
		assertThat(buildAnd(false, false).filter("")).isFalse();
	}

	private And buildAnd(final boolean a, final boolean b) {
		return Filters.and( //
				new Filter() {
					@Override
					public boolean filter(Object entity) {
						return a;
					}
				}, //
				new Filter() {
					@Override
					public boolean filter(Object entity) {
						return b;
					}
				}
		);
	}
}
