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

package net.cpollet.jixture.fixtures.capacities.extraction;

import net.cpollet.jixture.tests.mappings.Product;
import net.cpollet.jixture.tests.mappings.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestExtractionResult {
	private ExtractionResult extractionResult;

	private User user1, user2;
	private Product product1, product2;

	@Before
	public void setUp() {
		extractionResult = new ExtractionResult();

		user1 = new User();
		user2 = new User();
		product1 = new Product();
		product2 = new Product();

		extractionResult.add(user1, "name1");
		extractionResult.add(user2, "name2");
		extractionResult.add(product1, "name1");
		extractionResult.add(product2, "name2");
	}

	@Test
	public void getEntitiesByNameReturnsCorrectEntities() {
		// GIVEN
		// see @Before

		// WHEN
		List<Object> actualEntities = extractionResult.getEntities("name1");

		// THEN
		assertThat(actualEntities).containsOnly(user1, product1);
	}

	@Test
	public void getEntitiesByClassReturnsCorrectEntities() {
		// GIVEN
		// see @Before

		// WHEN
		List<Object> actualEntities = extractionResult.getEntities(User.class);

		// THEN
		assertThat(actualEntities).containsOnly(user1, user2);
	}

	@Test
	public void getEntitiesByNameAndClassReturnsCorrectEntities() {
		// GIVEN
		// see @Before

		// WHEN
		List<Object> actualEntities = extractionResult.getEntities("name1", User.class);

		// THEN
		assertThat(actualEntities).containsOnly(user1);
	}

	@Test
	public void getEntitiesReturnsCorrectEntities() {
		// GIVEN
		// see @Before

		// WHEN
		List<Object> actualEntities = extractionResult.getEntities();

		// THEN
		assertThat(actualEntities).containsOnly(user1, user2, product1, product2);
	}

	@Test
	public void mergeMergesExtractionResultNewObjectTypeExistingName() {
		// GIVEN
		// see @Before
		ExtractionResult anotherExtractionResult = new ExtractionResult();
		anotherExtractionResult.add("1", "name1");

		// WHEN
		extractionResult.merge(anotherExtractionResult);

		// THEN
		assertThat(extractionResult.getEntities("name1", String.class)).containsExactly("1");
	}

	@Test
	public void mergeMergesExtractionResultExistingObjectTypeExistingName() {
		// GIVEN
		// see @Before
		User user3 = new User();
		ExtractionResult anotherExtractionResult = new ExtractionResult();
		anotherExtractionResult.add(user3, "name1");

		// WHEN
		extractionResult.merge(anotherExtractionResult);

		// THEN
		assertThat(extractionResult.getEntities("name1", User.class)).containsExactly(user1, user3);
	}

	@Test
	public void mergeMergesExtractionResultExistingObjectTypeNewName() {
		// GIVEN
		// see @Before
		User user3 = new User();
		ExtractionResult anotherExtractionResult = new ExtractionResult();
		anotherExtractionResult.add(user3, "name3");

		// WHEN
		extractionResult.merge(anotherExtractionResult);

		// THEN
		assertThat(extractionResult.getEntities("name3", User.class)).containsExactly(user1, user3);
	}

	@Test
	public void mergeMergesExtractionResultNewObjectTypeNewName() {
		// GIVEN
		// see @Before
		ExtractionResult anotherExtractionResult = new ExtractionResult();
		anotherExtractionResult.add("3", "name3");

		// WHEN
		extractionResult.merge(anotherExtractionResult);

		// THEN
		assertThat(extractionResult.getEntities("name3", String.class)).containsExactly("3");
	}
}
