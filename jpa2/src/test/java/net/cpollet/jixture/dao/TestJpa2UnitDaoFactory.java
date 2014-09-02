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

package net.cpollet.jixture.dao;

import net.cpollet.jixture.jpa2.dao.Jpa2UnitDao;
import net.cpollet.jixture.jpa2.dao.Jpa2UnitDaoFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(MockitoJUnitRunner.class)
public class TestJpa2UnitDaoFactory {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private JpaTransactionManager transactionManager;

	@Mock
	private EntityManagerFactory entityManagerFactory;

	@Mock
	private EntityManager entityManager1;

	@Mock
	private EntityManager entityManager2;

	@Mock
	private Jpa2UnitDao unitDao;

	private Jpa2UnitDaoFactory unitDaoFactory;

	@Before
	public void setUp() {
		Mockito.when(transactionManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);

		unitDaoFactory = new Jpa2UnitDaoFactory();
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
		JpaTransactionManager actualTransactionManager = unitDaoFactory.getTransactionManager();

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
		assertThat(unitDao).isInstanceOf(Jpa2UnitDao.class);
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
	public void getUnitDaoReturnsAUnitDaoContainingAnEntityManager() throws Exception {
		// GIVEN
		Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager1);
		unitDaoFactory.afterPropertiesSet();

		// WHEN
		Jpa2UnitDao unitDao = (Jpa2UnitDao) unitDaoFactory.getUnitDao();

		// THEN
		assertThat(unitDao.getEntityManager()).isSameAs(entityManager1);
	}

	@Test
	public void getUnitDaoReturnsAUnitDaoContainingAnOpen() throws Exception {
		// GIVEN
		Mockito.when(entityManagerFactory.createEntityManager())//
				.thenReturn(entityManager1)//
				.thenReturn(entityManager2);

		Mockito.when(entityManager1.isOpen()).thenReturn(false);
		Mockito.when(entityManager2.isOpen()).thenReturn(true);

		unitDaoFactory.afterPropertiesSet();

		// WHEN
		Jpa2UnitDao unitDao = (Jpa2UnitDao) unitDaoFactory.getUnitDao();

		// THEN
		assertThat(unitDao.isEntityManagerOpen()).isFalse();
		assertThat(unitDao.getEntityManager()).isSameAs(entityManager1);

		// WHEN
		unitDao = (Jpa2UnitDao) unitDaoFactory.getUnitDao();

		// THEN
		assertThat(unitDao.isEntityManagerOpen()).isTrue();
		assertThat(unitDao.getEntityManager()).isSameAs(entityManager2);
	}
}
