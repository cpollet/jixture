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

package net.cpollet.jixture.fixtures.extraction;

import net.cpollet.jixture.tests.mappings.Client;
import net.cpollet.jixture.tests.mappings.Product;
import org.hamcrest.core.IsAnything;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */

public class TestExtractorDelegate {
	private ExtractorDelegate extractionDelegate;

	private Client client;
	private Product product;

	@Before
	public void setUp() {
		extractionDelegate = new ExtractorDelegate();

		client = new Client();
		product = new Product();

		extractionDelegate.addExtractorMatcher(ExtractorMatcher.create("clients", new IsInstanceOf(Client.class)));
		extractionDelegate.addExtractorMatcher(ExtractorMatcher.create("products", new IsInstanceOf(Product.class)));
		extractionDelegate.extractEntity(client);
		extractionDelegate.extractEntity(product);
		extractionDelegate.extractEntity("non extracted");
	}

	@Test
	public void getExtractionResultReturnsCorrectData() {
		// GIVEN
		// see @Before

		// WHEN
		ExtractionResult extractionResult = extractionDelegate.getExtractionResult();

		// THEN
		assertThat(extractionResult.getEntities("clients")).containsOnly(client);
		assertThat(extractionResult.getEntities("products")).containsOnly(product);
		assertThat(extractionResult.getEntities(Client.class)).containsOnly(client);
		assertThat(extractionResult.getEntities(Product.class)).containsOnly(product);
		assertThat(extractionResult.getEntities()).containsOnly(client, product);
	}
}
