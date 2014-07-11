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

import net.cpollet.jixture.fixtures.loaders.FixtureLoader;

/**
 * Allows to load data in commit mode. This class can be used as a helper class. An example of use is:
 * <pre>
 * <code>
 *     {@literal @}RunWith(SpringJUnit4ClassRunner.class)
 *     {@literal @}ContextConfiguration(locations = {"classpath:/spring/test-context.xml"})
 *     {@literal @}DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
 *     {@literal @}Transactional
 *     public class TestSimpleUserServiceWithoutSuperclass {
 *     	{@literal @}Autowired
 *     	private DatabaseTestSupport commitDatabaseTestSupport;
 *
 *     	{@literal @}Before
 *     	public void setUp() {
 *     		User user = new User();
 *     		user.setId(1L);
 *     		user.setName("name");
 *
 *     		commitDatabaseTestSupport
 *     		    .addFixtures(new MappingFixture(user))
 *     		    .loadFixtures();
 *     	}
 *
 *     	{@literal @}Test
 *     	public void testSomething() {
 *     		// the database contains our user.
 *     	}
 *     }
 * </code>
 * </pre>
 *
 * @see net.cpollet.jixture.support.NoCommitDatabaseTestSupport
 *
 * @author Christophe Pollet
 */
public class CommitDatabaseTestSupport extends BaseDatabaseTestSupport {
	/**
	 * Returns the commit mode. In this case, always
	 * {@link net.cpollet.jixture.fixtures.loaders.FixtureLoader.Mode#COMMIT}.
	 *
	 * @see net.cpollet.jixture.fixtures.loaders.FixtureLoader.Mode#COMMIT
	 *
	 * @return {@link net.cpollet.jixture.fixtures.loaders.FixtureLoader.Mode#COMMIT}
	 */
	@Override
	public FixtureLoader.Mode getCommitMode() {
		return FixtureLoader.Mode.COMMIT;
	}
}
