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

import net.cpollet.jixture.fixtures.transformers.FixtureTransformer;
import org.apache.commons.io.FileUtils;
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
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestXmlFileFixture {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Mock
	private FixtureTransformer fixtureTransformer;

	@Test
	public void getInputStreamReturnsInputStreamFromClasspathResource() throws IOException {
		// GIVEN
		XmlFileFixture xmlFileFixture = new XmlFileFixture("classpath:tests/fixtures/spring-fixture.xml");

		// WHEN
		InputStream actualInputStream = xmlFileFixture.getInputStream();

		// THEN
		InputStream expectedInputStream = getClass().getResourceAsStream("/tests/fixtures/spring-fixture.xml");

		compareInputStreams(actualInputStream, expectedInputStream);
	}

	private void compareInputStreams(InputStream actual, InputStream expected) throws IOException {
		int actualByte = actual.read();
		int expectedByte = expected.read();

		while (actualByte != -1 || expectedByte != -1) {
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
		XmlFileFixture xmlFileFixture = new XmlFileFixture(filePath);

		// WHEN
		InputStream actualInputStream = xmlFileFixture.getInputStream();

		// THEN
		InputStream expectedInputStream = new FileInputStream(filePath);
		compareInputStreams(actualInputStream, expectedInputStream);
	}

	@Test
	public void getTransformedFixtureReturnsTransformedFixture() {
		// GIVEN
		Fixture expectedTransformedFixture = new AbstractFixture() {
			@Override
			public Fixture addObjects(Object... objects) {
				return this;
			}

			@Override
			public List<Object> getObjects() {
				return null;
			}
		};

		XmlFileFixture xmlFileFixture = new XmlFileFixture("classpath:tests/fixtures/spring-fixture.xml");
		Mockito.when(fixtureTransformer.transform(xmlFileFixture)).thenReturn(expectedTransformedFixture);

		// WHEN
		Fixture actualTransformedFixture = fixtureTransformer.transform(xmlFileFixture);

		// THEN
		assertThat(actualTransformedFixture).isSameAs(expectedTransformedFixture);
	}
}
