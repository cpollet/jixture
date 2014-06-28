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

package net.cpollet.jixture.support;


import org.junit.After;
import org.junit.Before;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractDatabaseTest extends AbstractTestSupport implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	@Before
	public void beforeTest() {
		setupFixtures();
		super.beforeTest();
	}

	@Override
	@After
	public void afterTest() {
		teardownFixtures();
		super.afterTest();
	}

	public <T> T getBean(Class<T> type) {
		return (T) applicationContext.getBean(type);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
