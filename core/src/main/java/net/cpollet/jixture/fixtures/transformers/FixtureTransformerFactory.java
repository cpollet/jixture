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

package net.cpollet.jixture.fixtures.transformers;

import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.TransformableFixture;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Christophe Pollet
 */
public class FixtureTransformerFactory implements InitializingBean {
	@Autowired
	private List<FixtureTransformer> transformers;

	private Map<Class, FixtureTransformer> transformersMap;

	public FixtureTransformer getFixtureTransformer(TransformableFixture fixture) {
		assertTransformerIsDefined(fixture);

		return transformersMap.get(fixture.getClass());
	}

	private void assertTransformerIsDefined(TransformableFixture fixture) {
		if (!transformersMap.containsKey(fixture.getClass())) {
			throw new RuntimeException("No fixture transformer defined for " + fixture.getClass().getName());
		}
	}

	public FixtureTransformer getFixtureTransformer(@SuppressWarnings("unused") Fixture fixture) {
		return transformersMap.get(Fixture.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		transformersMap = new HashMap<Class, FixtureTransformer>();

		for (FixtureTransformer fixtureTransformer : transformers) {
			transformersMap.put(fixtureTransformer.getFromType(), fixtureTransformer);
		}

		transformersMap.put(FIXTURE_TRANSFORMER.getFromType(), FIXTURE_TRANSFORMER);
	}

	private static final FixtureTransformer FIXTURE_TRANSFORMER = new FixtureTransformer<Fixture>() {
		@Override
		public Class getFromType() {
			return Fixture.class;
		}

		@Override
		public Fixture transform(Fixture fixture) {
			return fixture;
		}
	};
}
