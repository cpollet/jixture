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

package net.cpollet.jixture.hibernate3.dao;

import net.cpollet.jixture.tests.mappings.Product;
import net.cpollet.jixture.tests.mappings.User;
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
@ContextConfiguration({"classpath:/spring/hibernate3-unit-test-context.xml"})
@Transactional
public class TestHibernate3UnitDao {
	@Autowired
	private SessionFactory sessionFactory;

	private Hibernate3UnitDao unitDao;

	private User user1;
	private User user2;
	private Product product;

	@Before
	public void setUp() {
		unitDao = new Hibernate3UnitDao();
		unitDao.setSession(sessionFactory.getCurrentSession());

		user1 = new User();
		user1.setUsername("user1");
		user1.setPassword("oldPassword");

		user2 = new User();
		user2.setUsername("user2");

		product = new Product();
		product.setId("id");
		product.setName("oldName");

		sessionFactory.getCurrentSession().save(user1);
		sessionFactory.getCurrentSession().save(user2);
		sessionFactory.getCurrentSession().save(product);
		unitDao.flushAndClear();
	}

	@Test
	public void getAllReturnsAllRows() {
		// GIVEN
		// see @Before

		// WHEN
		List<User> users = unitDao.getAll(User.class);

		// THEN
		assertThat(users) //
				.hasSize(2) //
				.contains(user1, user2);
	}

	@Test
	public void getOneReturnsCorrectEntity() {
		// GIVEN
		// see @Before

		// WHEN
		User user = unitDao.getOne(User.class, "user1");

		// THEN
		assertThat(user).isEqualTo(user1);
	}

	@Test
	public void saveActuallySavesAnEntity() {
		// GIVEN
		User user = new User();
		user.setUsername("user3");

		// WHEN
		unitDao.save(user);

		// THEN
		User actualUser = (User) sessionFactory.getCurrentSession().get(User.class, "user3");
		assertThat(actualUser).isSameAs(user);
	}

	@Test
	public void deleteActuallyDeletesAnEntity() {
		// GIVEN
		// see @Before

		// WHEN
		unitDao.delete(user1);

		// THEN
		List<User> remainingUsers = unitDao.getAll(User.class);

		assertThat(remainingUsers) //
				.hasSize(1) //
				.contains(user2);
	}

	@Test
	public void deleteAllDeletesAllEntities() {
		// GIVEN
		// see @Before

		// WHEN
		unitDao.deleteAll(User.class);

		// THEN
		List<User> remainingUsers = unitDao.getAll(User.class);
		assertThat(remainingUsers).hasSize(0);
	}

	@Test
	public void executesActuallyExecutesTheQuery() {
		// GIVEN
		// see @Before

		// WHEN
		unitDao.execute("update products set name='newName' where id='id'");

		// THEN
		Product product = (Product) sessionFactory.getCurrentSession().get(Product.class, "id");
		assertThat(product.getName()).isEqualTo("newName");
	}

}
