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

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * @author Christophe Pollet
 */
public class JixtureTestExecutionListener implements TestExecutionListener {
	private JixtureTestExecutionListenerSetupHandler setupHandler;
	private JixtureTestExecutionListenerCleanupHandler cleanupHandler;

	public JixtureTestExecutionListener() {
		setupHandler = new JixtureTestExecutionListenerSetupHandler();
		cleanupHandler = new JixtureTestExecutionListenerCleanupHandler();
	}

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		// no-op
	}

	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
		// no-op
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		setupHandler.handle(testContext);
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		cleanupHandler.handle(testContext);
	}

	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		// no-op
	}

}
