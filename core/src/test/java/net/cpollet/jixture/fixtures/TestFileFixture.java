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

package net.cpollet.jixture.fixtures;

import net.cpollet.jixture.fixtures.capacities.extraction.ExtractionResult;
import net.cpollet.jixture.fixtures.capacities.extraction.ExtractorMatcher;
import net.cpollet.jixture.fixtures.transformers.FixtureTransformer;
import org.apache.commons.io.FileUtils;
import org.hamcrest.core.IsAnything;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class TestFileFixture {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Mock
	private FixtureTransformer fixtureTransformer;

	@Test
	public void getInputStreamReturnsInputStreamFromClasspathResource() throws IOException {
		// GIVEN
		FileFixture fileFixture = buildFixture("classpath:tests/fixtures/spring-fixture.xml");

		// WHEN
		InputStream actualInputStream = fileFixture.getInputStream();

		// THEN
		InputStream expectedInputStream = getClass().getResourceAsStream("/tests/fixtures/spring-fixture.xml");

		compareInputStreams(actualInputStream, expectedInputStream);
	}

	protected abstract FileFixture buildFixture(String filePath);

	private void compareInputStreams(InputStream actual, InputStream expected) throws IOException {
		int actualByte = actual.read();
		int expectedByte = expected.read();

		while (-1 != actualByte || -1 != expectedByte) {
			assertThat(actualByte).isEqualTo(expectedByte);
			actualByte = actual.read();
			expectedByte = expected.read();
		}
	}

	@Test
	public void getInputStreamReturnsInputStreamFromPath() throws IOException {
		// GIVEN
		File file = folder.newFile("foo.txt");
		FileUtils.writeStringToFile(file, "someContent");

		String filePath = file.getAbsoluteFile().getAbsolutePath();
		FileFixture fileFixture = buildFixture(filePath);

		// WHEN
		InputStream actualInputStream = fileFixture.getInputStream();

		// THEN
		InputStream expectedInputStream = new FileInputStream(filePath);
		compareInputStreams(actualInputStream, expectedInputStream);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getTransformedFixtureReturnsTransformedFixture() {
		// GIVEN
		Fixture expectedTransformedFixture = new BaseObjectFixture() {
			@Override
			public Fixture addObjects(Object... objects) {
				return this;
			}

			@Override
			public List<Object> getObjects() {
				return null;
			}
		};

		FileFixture fileFixture = buildFixture("classpath:tests/fixtures/spring-fixture.xml");
		Mockito.when(fixtureTransformer.transform(fileFixture)).thenReturn(expectedTransformedFixture);

		// WHEN
		Fixture actualTransformedFixture = fixtureTransformer.transform(fileFixture);

		// THEN
		assertThat(actualTransformedFixture).isSameAs(expectedTransformedFixture);
	}

	@Test
	public void getExtractionResultReturnCorrectEntities() throws IOException {
		// GIVEN
		File file = folder.newFile("foo.txt");
		FileUtils.writeStringToFile(file, "someContent");

		String filePath = file.getAbsoluteFile().getAbsolutePath();
		FileFixture fileFixture = buildFixture(filePath);
		fileFixture.addExtractorMatcher(ExtractorMatcher.create(new IsAnything()));
		fileFixture.populateExtractionResult(Arrays.<Object>asList("string1", "string2"));

		// WHEN
		ExtractionResult extractionResult = fileFixture.getExtractionResult();

		// THEN
		assertThat(extractionResult.getEntities()).containsOnly("string1", "string2");
	}
}
