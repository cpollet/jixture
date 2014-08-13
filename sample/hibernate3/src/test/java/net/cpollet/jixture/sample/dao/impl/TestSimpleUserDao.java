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

package net.cpollet.jixture.sample.dao.impl;

import net.cpollet.jixture.sample.da.dao.impl.SimpleUserDao;
import net.cpollet.jixture.sample.da.data.User;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test-context.xml"})
@Transactional
public class TestSimpleUserDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private SimpleUserDao simpleUserDao;

	private User user;

	@Before
	public void setUp() {
		user  = new User();
		user.setId(1L);
		user.setCount(0);
		user.setName("name");

		sessionFactory.getCurrentSession().save(user);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}

	@Test
	public void findAllReturnsAllUsers() {
		// GIVEN
		// see @Before

		// WHEN
		List<User> users = simpleUserDao.findAll();

		// THEN
		assertThat(users).hasSize(1);
	}
}
