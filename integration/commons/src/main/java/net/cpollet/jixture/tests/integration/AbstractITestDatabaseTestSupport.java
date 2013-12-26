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

package net.cpollet.jixture.tests.integration;

import net.cpollet.jixture.dao.SimpleUnitDao;
import net.cpollet.jixture.dao.UnitDao;
import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.support.CommitDatabaseTestSupport;
import net.cpollet.jixture.support.NoCommitDatabaseTestSupport;
import net.cpollet.jixture.tests.mappings.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("classpath:/spring/integration-context.xml")
public abstract class AbstractITestDatabaseTestSupport {
	private static final Logger logger = LoggerFactory.getLogger(AbstractITestDatabaseTestSupport.class);

	@Autowired
	private HibernateTransactionManager transactionManager;

	@Autowired
	private NoCommitDatabaseTestSupport noCommitDatabaseTestSupport;

	@Autowired
	private CommitDatabaseTestSupport commitDatabaseTestSupport;

	@Before
	public void setUp() {
		logger.trace("> setUp()");
		cleanup();
		logger.trace("< setUp()");
	}

	private void cleanup() {
		executeInNewTransaction(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				UnitDao unitDao = new SimpleUnitDao();
				unitDao.setSession(transactionManager.getSessionFactory().getCurrentSession());

				unitDao.deleteAll(User.class);
			}
		});

		assertDatabaseDoesNotContainUser();
	}

	@Test
	public void noCommitDatabaseTestSupportDoesNotCommit() {
		noCommitDatabaseTestSupport.setFixtures(Arrays.asList(new MappingFixture(createUser())));
		noCommitDatabaseTestSupport.beforeTest();

		assertDatabaseContainsUser();

		executeInNewTransaction(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertDatabaseDoesNotContainUser();
			}
		});
	}

	private User createUser() {
		User user = new User();
		user.setUsername("username");
		user.setConnectionsCount(1);
		return user;
	}

	private void assertDatabaseContainsUser() {
		List<User> users = getUsers();
		assertThat(users.size()).isEqualTo(1);
	}

	private List<User> getUsers() {
		return transactionManager.getSessionFactory().getCurrentSession().createCriteria(User.class).list();
	}

	private void executeInNewTransaction(TransactionCallback transactionCallback) {
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

		transactionTemplate.execute(transactionCallback);
	}

	private void assertDatabaseDoesNotContainUser() {
		List<User> users = getUsers();
		assertThat(users.size()).isEqualTo(0);
	}

	@Test
	public void commitDatabaseSupportCommits() {
		commitDatabaseTestSupport.setFixtures(Arrays.asList(new MappingFixture(createUser())));
		commitDatabaseTestSupport.beforeTest();

		assertDatabaseContainsUser();

		executeInNewTransaction(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				assertDatabaseContainsUser();
			}
		});

		cleanup();
	}
}
