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

package net.cpollet.jixture.fixtures.filter;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestOr {
	@Test
	public void filter() {
		assertThat(buildAnd(true, true).filter("")).isTrue();
		assertThat(buildAnd(true, false).filter("")).isTrue();
		assertThat(buildAnd(false, true).filter("")).isTrue();
		assertThat(buildAnd(false, false).filter("")).isFalse();
	}

	private Or buildAnd(final boolean a, final boolean b) {
		return Filters.or( //
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
