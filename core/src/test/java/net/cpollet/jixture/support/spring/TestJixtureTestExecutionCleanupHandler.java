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

package net.cpollet.jixture.support.spring;

import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.XlsxFileFixture;
import net.cpollet.jixture.fixtures.XmlFileFixture;
import net.cpollet.jixture.fixtures.loaders.FixtureLoader;
import net.cpollet.jixture.support.DatabaseTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.JixtureTestContext;
import org.springframework.test.context.TestContext;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestJixtureTestExecutionCleanupHandler {
	private JixtureTestExecutionListenerCleanupHandler handler;

	@Mock
	private ApplicationContext applicationContext;

	@Mock
	private DatabaseTestSupport noCommitDatabaseTestSupport;

	@Mock
	private DatabaseTestSupport commitDatabaseTestSupport;

	@Before
	public void setUp() {
		handler = new JixtureTestExecutionListenerCleanupHandler();

		Map<FixtureLoader.Mode, DatabaseTestSupport> databaseTestSupportMap = new HashMap<FixtureLoader.Mode, DatabaseTestSupport>();
		databaseTestSupportMap.put(FixtureLoader.Mode.NO_COMMIT, noCommitDatabaseTestSupport);
		databaseTestSupportMap.put(FixtureLoader.Mode.COMMIT, commitDatabaseTestSupport);

		Mockito.when(applicationContext.getBean("jixture.databaseTestSupportByMode")).thenReturn(databaseTestSupportMap);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void jixtureLoadsInOrderAndRespectsMode() throws Exception {
		// GIVEN
		TestContext testContext = buildDefaultTestContext(TestClassHolder.TestClassForDefaultXmlAndXlsxFilePaths.class);

		// WHEN
		handler.handle(testContext);

		// THEN
		ArgumentCaptor<List> argumentCaptorForCommit = ArgumentCaptor.forClass(List.class);
		Mockito.verify(commitDatabaseTestSupport).addFixtures(argumentCaptorForCommit.capture());
		Mockito.verify(commitDatabaseTestSupport).loadFixtures();

		ArgumentCaptor<List> argumentCaptorForNoCommit = ArgumentCaptor.forClass(List.class);
		Mockito.verify(noCommitDatabaseTestSupport).addFixtures(argumentCaptorForNoCommit.capture());
		Mockito.verify(noCommitDatabaseTestSupport).loadFixtures();

		List<Fixture> committedFixtures = flatten(argumentCaptorForCommit.getAllValues());
		List<Fixture> nonCommittedFixtures = flatten(argumentCaptorForNoCommit.getAllValues());

		assertThat(committedFixtures.size()).isEqualTo(1);
		assertThat(committedFixtures.get(0)).isNotNull();
		assertThat(committedFixtures.get(0)).isInstanceOf(XmlFileFixture.class);

		assertThat(nonCommittedFixtures.size()).isEqualTo(1);
		assertThat(nonCommittedFixtures.get(0)).isNotNull();
		assertThat(nonCommittedFixtures.get(0)).isInstanceOf(XlsxFileFixture.class);
	}

	private JixtureTestContext buildDefaultTestContext(Class clazz) {
		try {
			return new JixtureTestContext(clazz, Object.class.getMethod("toString"), applicationContext);
		}
		catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private List flatten(List<List> listOfLists) {
		List flattenedList = new LinkedList();

		for (List innerList : listOfLists) {
			flattenedList.addAll(innerList);
		}

		return flattenedList;
	}
}
