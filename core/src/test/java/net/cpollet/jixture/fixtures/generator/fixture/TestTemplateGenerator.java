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

import net.cpollet.jixture.fixtures.generator.field.FieldGenerator;
import net.cpollet.jixture.fixtures.generator.field.IntegerSequence;
import net.cpollet.jixture.tests.mappings.CartEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestTemplateGenerator {
	private TemplateGenerator templateGenerator;

	@Mock
	private FieldGenerator fieldGenerator;

	private CartEntry cartEntry;

	@Before
	public void setUp() {
		cartEntry = new CartEntry();
		cartEntry.setPk(new CartEntry.CartEntryPk());
		cartEntry.getPk().setClientId(1);
		cartEntry.getPk().setProductId(2);
		cartEntry.setCount(0);
		templateGenerator = new TemplateGenerator(cartEntry);
	}

	@Test
	public void hasNextReturnsFalseWhenNoFieldGeneratorAreSet() {
		// GIVEN
		// see @Before

		// WHEN
		boolean actualResult = templateGenerator.hasNext();

		// THEN
		assertThat(actualResult).isFalse();
	}

	@Test
	public void hasNextReturnsFalseWhenFieldGeneratorsDontHaveNext() {
		// GIVEN
		Mockito.when(fieldGenerator.hasNext()).thenReturn(false);
		templateGenerator.addFieldGenerator("field", fieldGenerator);

		// WHEN
		boolean actualResult = templateGenerator.hasNext();

		// THEN
		assertThat(actualResult).isFalse();
	}

	@Test
	public void hasNextReturnsTrueWhenFieldGeneratorsHaveNext() {
		// GIVEN
		Mockito.when(fieldGenerator.hasNext()).thenReturn(true);
		templateGenerator.addFieldGenerator("field", fieldGenerator);

		// WHEN
		boolean actualResult = templateGenerator.hasNext();

		// THEN
		assertThat(actualResult).isTrue();
	}

	@Test
	public void getGeneratedClassReturnsCorrectValue() {
		// GIVEN
		templateGenerator = new TemplateGenerator("");

		// WHEN
		Class actualValue = templateGenerator.getGeneratedClass();

		// THEN
		assertThat(actualValue).isEqualTo(String.class);
	}

	@Test
	public void nextReturnsCorrectObject() {
		// GIVEN
		templateGenerator.addFieldGenerator("count", new IntegerSequence(1, 2));

		// WHEN
		CartEntry cartEntry1 = (CartEntry) templateGenerator.next();
		CartEntry cartEntry2 = (CartEntry) templateGenerator.next();

		// THEN
		assertThat(cartEntry1.getCount()).isEqualTo(1);
		assertThat(cartEntry2.getCount()).isEqualTo(2);

		assertThat(cartEntry1.getPk()) //
				.isNotSameAs(cartEntry.getPk()) //
				.isNotSameAs(cartEntry2.getPk()) //
				.isEqualTo(cartEntry.getPk());
		assertThat(cartEntry2.getPk()) //
				.isNotSameAs(cartEntry.getPk()) //
				.isNotSameAs(cartEntry1.getPk()) //
				.isEqualTo(cartEntry.getPk());
	}
}
