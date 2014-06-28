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

package net.cpollet.jixture.sample.service.impl;

import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.sample.da.data.User;
import net.cpollet.jixture.sample.service.UserService;
import net.cpollet.jixture.support.AbstractCommitDatabaseTest;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-context.xml"})
@Transactional
public class TestSimpleUserServiceWithSuperclass extends AbstractCommitDatabaseTest {
	@Autowired
	private UserService userService;

	@Autowired
	private SessionFactory sessionFactory;

	@Before
	public void setUp() {
		User uncommittedUser = new User();
		uncommittedUser.setId(2L);
		uncommittedUser.setName("name");
		uncommittedUser.setCount(0);

		sessionFactory.getCurrentSession().save(uncommittedUser);
	}

	@Override
	protected void setupFixtures() {
		User committedUser = new User();
		committedUser.setId(1L);
		committedUser.setName("name");
		committedUser.setCount(0);

		addFixtures(new MappingFixture(committedUser));
	}

	@Test
	public void getUsersCountReturnsCorrectResult() {
		// GIVEN
		// see @Before

		// WHEN
		int count = userService.getUsersCount();

		// THEN
		assertThat(count).isEqualTo(2);
	}

	@Test
	public void getCommittedUsersCountReturnsCorrectResult() {
		// GIVEN
		// see @Before

		// WHEN
		int count = userService.getCommittedUsersCount();

		// THEN
		assertThat(count).isEqualTo(1);
	}
}
