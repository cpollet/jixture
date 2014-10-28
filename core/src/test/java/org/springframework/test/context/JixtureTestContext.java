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

package org.springframework.test.context;

import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

/**
 * @author Christophe Pollet
 */

/**
 * Kind of dirty actually, but it's an easy way to build a minimal test-specific TestContext.
 */
public class JixtureTestContext extends TestContext {
	private ApplicationContext applicationContext;

	public JixtureTestContext(Class<?> testClass, ApplicationContext applicationContext) {
		super(testClass, buildContextCache());
		this.applicationContext = applicationContext;
	}

	public JixtureTestContext(Class<?> testClass, Method method, ApplicationContext applicationContext) {
		super(testClass, buildContextCache());
		this.applicationContext = applicationContext;
		updateState(null, method, null);
	}

	/**
	 * Returns a new empty context cache. We don't need it actually, since we give the application context to use.
	 *
	 * @return an empty ContextCache instance.
	 */
	private static ContextCache buildContextCache() {
		return new ContextCache();
	}

	@Override
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
