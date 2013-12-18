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

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestSpringFixture {
	@Test
	public void getContextReturnsTheContextWhenConstructedWithVarargs() {
		// GIVEN
		SpringFixture springFixture = new SpringFixture("context", String.class);

		// WHEN + THEN
		assertThat(springFixture.getContext()).isEqualTo("context");
	}

	@Test
	public void getContextReturnsTheContextWhenConstructedWithList() {
		// GIVEN
		SpringFixture springFixture = new SpringFixture("context", Arrays.<Class<?>>asList(String.class));

		// WHEN + THEN
		assertThat(springFixture.getContext()).isEqualTo("context");
	}

	@Test
	public void getContextReturnsTheClassesListWhenConstructedWithVarargs() {
		// GIVEN
		SpringFixture springFixture = new SpringFixture("context", String.class, Integer.class);

		// WHEN
		List<Class<?>> classList = springFixture.getClasses();

		// THEN
		assertThat(classList)//
				.hasSize(2)//
				.containsSequence(String.class, Integer.class);
	}

	@Test
	public void getContextReturnsTheClassesListWhenConstructedWithList() {
		// GIVEN
		SpringFixture springFixture = new SpringFixture("context", Arrays.<Class<?>>asList(String.class, Integer.class));

		// WHEN
		List<Class<?>> classList = springFixture.getClasses();

		// THEN
		assertThat(classList)//
				.hasSize(2)//
				.containsSequence(String.class, Integer.class);
	}
}
