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

package net.cpollet.jixture.spring;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author Christophe Pollet
 */
public class TestConfiguration {
	private final static Logger logger = LoggerFactory.getLogger(TestConfiguration.class);

	@Test
	public void springConfigrationIsWorking() {
		GenericApplicationContext applicationContext = new GenericApplicationContext(new ClassPathXmlApplicationContext("classpath:/spring/mockedTransactionManager-context.xml"));


		for (String beanName : applicationContext.getBeanDefinitionNames()) {
			if (!applicationContext.getBeanDefinition(beanName).isAbstract()) {
				logger.info("Requesting bean {}", beanName);
				applicationContext.getBean(beanName);
			}
		}
	}
}
