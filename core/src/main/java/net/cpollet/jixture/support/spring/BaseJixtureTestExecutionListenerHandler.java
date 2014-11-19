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
import net.cpollet.jixture.fixtures.loaders.FixtureLoader;
import net.cpollet.jixture.support.DatabaseTestSupport;
import net.cpollet.jixture.support.spring.annotation.FixtureDef;
import net.cpollet.jixture.support.spring.annotation.Jixture;
import org.springframework.test.context.TestContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author Christophe Pollet
 */
public abstract class BaseJixtureTestExecutionListenerHandler {
	public abstract void handle(TestContext testContext) throws Exception;

	protected List<? extends Fixture> buildFixture(String fixtureType, Object[] parameters) {
		return FixtureType.getByName(fixtureType)//
				.getFixtureBuilderBuilder()//
				.setUp(parameters)//
				.build();
	}

	@SuppressWarnings("unchecked")
	protected DatabaseTestSupport getDatabaseTestSupport(TestContext testContext, FixtureLoader.Mode mode) {
		Map<FixtureLoader.Mode, DatabaseTestSupport> databaseTestSupports = (Map<FixtureLoader.Mode, DatabaseTestSupport>) testContext.getApplicationContext().getBean("jixture.databaseTestSupportByMode");
		return databaseTestSupports.get(mode);
	}

	protected void loadFixture(TestContext testContext, FixtureDef fixtureDef) throws Exception {
		if (null == fixtureDef) {
			return;
		}

		DatabaseTestSupport databaseTestSupport = getDatabaseTestSupport(testContext, fixtureDef.mode());

		for (String fixtureType : fixtureDef.order()) {
			Method method = fixtureDef.getClass().getMethod(fixtureType);

			Object[] parameters = (Object[]) method.invoke(fixtureDef);
			if (0 < parameters.length) {
				List<? extends Fixture> fixtures = buildFixture(fixtureType, parameters);

				databaseTestSupport.addFixtures(fixtures);
			}
		}

		databaseTestSupport.loadFixtures();
	}


}
