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

package net.cpollet.jixture.dao;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestUnitDaoFactory {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private HibernateTransactionManager transactionManager;

	@Mock
	private SessionFactory sessionFactory;

	@Mock
	private Session session1;

	@Mock
	private Session session2;

	@Mock
	private UnitDao unitDao;

	private UnitDaoFactory unitDaoFactory;

	@Before
	public void setUp() {
		Mockito.when(transactionManager.getSessionFactory()).thenReturn(sessionFactory);

		unitDaoFactory = new UnitDaoFactory();
		unitDaoFactory.setTransactionManager(transactionManager);
	}

	@Test
	public void transactionManagerIsMandatory() throws Exception {
		// GIVEN
		unitDaoFactory.setTransactionManager(null);

		// THEN
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("transactionManager must be set");

		// WHEN
		unitDaoFactory.afterPropertiesSet();
	}

	@Test
	public void getTransactionManagerReturnsTheTransactionManager() {
		// GIVEN

		// WHEN
		HibernateTransactionManager actualTransactionManager = unitDaoFactory.getTransactionManager();

		// THEN
		assertThat(actualTransactionManager).isSameAs(transactionManager);
	}


	@Test
	public void getUnitDaoWithoutUnitDaoProvided() throws Exception {
		// GIVEN
		unitDaoFactory.afterPropertiesSet();

		// WHEN
		UnitDao unitDao = unitDaoFactory.getUnitDao();

		// THEN
		assertThat(unitDao).isInstanceOf(SimpleUnitDao.class);
	}

	@Test
	public void getUnitDaoWithUnitDaoProvidedReturnsIt() throws Exception {
		// GIVEN
		unitDaoFactory.setUnitDao(unitDao);
		unitDaoFactory.afterPropertiesSet();

		// WHEN
		UnitDao actualUnitDao = unitDaoFactory.getUnitDao();

		// THEN
		assertThat(actualUnitDao).isSameAs(unitDao);
	}

	@Test
	public void getUnitDaoReturnsAUnitDaoContainingASession() throws Exception {
		// GIVEN
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session1);
		unitDaoFactory.afterPropertiesSet();

		// WHEN
		UnitDao unitDao = unitDaoFactory.getUnitDao();

		// THEN
		assertThat(unitDao.getSession()).isSameAs(session1);
	}

	@Test
	public void getUnitDaoReturnsAUnitDaoContainingAnOpen() throws Exception {
		// GIVEN
		Mockito.when(sessionFactory.getCurrentSession())//
				.thenReturn(session1)//
				.thenReturn(session2);

		Mockito.when(session1.isOpen()).thenReturn(false);
		Mockito.when(session2.isOpen()).thenReturn(true);

		unitDaoFactory.afterPropertiesSet();

		// WHEN
		UnitDao unitDao = unitDaoFactory.getUnitDao();

		// THEN
		assertThat(unitDao.isSessionOpen()).isFalse();
		assertThat(unitDao.getSession()).isSameAs(session1);

		// WHEN
		unitDao = unitDaoFactory.getUnitDao();

		// THEN
		assertThat(unitDao.isSessionOpen()).isTrue();
		assertThat(unitDao.getSession()).isSameAs(session2);
	}
}
