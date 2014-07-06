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

/**
 * Can be used as a base class for test classes needing database and fixture loading mechanisms. As example of use is:
 * <pre>
 * <code>
 *     {@literal @}RunWith(SpringJUnit4ClassRunner.class)
 *     {@literal @}ContextConfiguration(locations = {"classpath:/spring/test-context.xml"})
 *     {@literal @}Transactional
 *     public class TestSimpleUserServiceWithSuperclass extends AbstractCommitDatabaseTest {
 *     	{@literal @}Override
 *     	public void setupFixtures() {
 *     		User user = new User();
 *     		user.setId(1L);
 *     		user.setName("name");
 *     		addFixtures(new MappingFixture(user));
 *     	}
 *
 *     	{@literal @}Test
 *     	public void testSomething() {
 *     		// the database contains our user.
 *     	}
 *     }
 * </code>
 * </pre>
 * If you don't want the user entity to be committed, you can extend
 * {@link net.cpollet.jixture.support.AbstractNoCommitDatabaseTest} instead.
 *
 * @see net.cpollet.jixture.support.AbstractCommitDatabaseTest
 * @see net.cpollet.jixture.support.AbstractNoCommitDatabaseTest
 */
@Transactional
public abstract class AbstractDatabaseTest extends AbstractTestSupport implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	/**
	 * Load fixtures. Executed before each test method. {@link #setupFixtures()} is called before, allowing the subclass
	 * to prepare the fixture before its loading, if needed.
	 */
	@Override
	@Before
	public void beforeTest() {
		setupFixtures();
		super.beforeTest();
	}

	/**
	 * Cleanup loaded fixtures. Executed after each test method. Note that this method does not necessarily restore
	 * database content. Its main use is to reset fixture's internal state. {@link #teardownFixtures()} is called
	 * before, allowing the subclass to do additional work before the fixture is reset.
	 */
	@Override
	@After
	public void afterTest() {
		teardownFixtures();
		super.afterTest();
	}

	/**
	 * Returns a Spring bean from the current context.
	 * @param type the Spring bean type.
	 * @return a Spring bean from the current context.
	 */
	public <T> T getBean(Class<T> type) {
		return (T) applicationContext.getBean(type);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
