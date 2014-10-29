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

package net.cpollet.jixture.tests.integration.hibernate3;

import net.cpollet.jixture.dao.UnitDaoFactory;
import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.fixtures.loaders.FixtureLoader;
import net.cpollet.jixture.support.hook.FixtureBuilder;
import net.cpollet.jixture.support.hook.JixtureTestExecutionListener;
import net.cpollet.jixture.support.hook.PrepareData;
import net.cpollet.jixture.tests.mappings.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring/hibernate3-integration-context.xml")
@TestExecutionListeners(listeners = {//
		DependencyInjectionTestExecutionListener.class,//
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,//
		JixtureTestExecutionListener.class})
@PrepareData(//
		mode = FixtureLoader.Mode.NO_COMMIT,//
		builders = BaseTestPrepareData.MyBuilder.class//
)
@Transactional
public abstract class BaseTestPrepareData {
	@Autowired
	private UnitDaoFactory unitDaoFactory;

	@Test
	public void dataLoadedThroughAnnotation() {
		// GIVEN see @PrepareData

		// WHEN
		List<User> users = unitDaoFactory.getUnitDao().getAll(User.class);

		assertThat(users).hasSize(1);
	}

	public static class MyBuilder implements FixtureBuilder {

		@Override
		public List<? extends Fixture> build() {
			return Arrays.asList(//
					new MappingFixture(createUser())//
			);
		}

		private User createUser() {
			User user = new User();
			user.setUsername("username");
			user.setConnectionsCount(1);
			return user;
		}
	}
}
