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

package net.cpollet.jixture.fixtures.loaders;

import net.cpollet.jixture.fixtures.Fixture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Christophe Pollet
 */
@Component
public class FixtureLoaderChain implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(FixtureLoaderChain.class);

	@Autowired(required = false)
	private List<FixtureLoader> loaders;

	public void load(Fixture fixture, FixtureLoader.Mode mode) {
		LoadableFixture loadableFixture = new LoadableFixture(fixture, mode);
		Iterator<FixtureLoader> fixtureLoaderIterator = loaders.iterator();

		while (!loadableFixture.isLoaded()) {
			loadWithNextLoader(loadableFixture, fixtureLoaderIterator);
		}
	}

	private void loadWithNextLoader(LoadableFixture loadableFixture, Iterator<FixtureLoader> fixtureLoaderIterator) {
		FixtureLoader loader = getNextFixtureLoader(fixtureLoaderIterator, loadableFixture.getType());
		loadableFixture.tryToLoad(loader);
	}

	private FixtureLoader getNextFixtureLoader(Iterator<FixtureLoader> fixtureLoaderIterator, String fixtureType) {
		try {
			return fixtureLoaderIterator.next();
		}
		catch (NoSuchElementException e) {
			throw new NoSuchElementException("Unable to find loader for fixture of type " + fixtureType);
		}
	}

	public void resetLoaders() {
		for (FixtureLoader loader : loaders) {
			loader.reset();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (loaders == null) {
			loaders = Collections.emptyList();
		}

		if (loaders.isEmpty()) {
			logger.warn("No fixture loader defined");
		}
	}

	private static class LoadableFixture {
		private boolean loaded;
		private Fixture fixture;
		private FixtureLoader.Mode mode;

		public LoadableFixture(Fixture fixture, FixtureLoader.Mode mode) {
			this.loaded = false;
			this.fixture = fixture;
			this.mode = mode;
		}

		public boolean isLoaded() {
			return loaded;
		}

		public String getType() {
			return fixture.getClass().getName();
		}

		public void tryToLoad(FixtureLoader loader) {
			if (loader.canLoad(fixture)) {
				loader.load(fixture, mode);
				loaded = true;
			}
		}
	}
}
