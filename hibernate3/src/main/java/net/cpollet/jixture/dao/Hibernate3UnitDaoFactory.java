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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.util.Assert;

/**
 * @author Christophe Pollet
 */
public class Hibernate3UnitDaoFactory implements UnitDaoFactory, InitializingBean {
	private final static Logger logger = LoggerFactory.getLogger(Hibernate3UnitDaoFactory.class);

	@Autowired
	@Qualifier(value = "jixture.core.transactionManager")
	private HibernateTransactionManager transactionManager;

	private Hibernate3UnitDao unitDao;

	@Override
	public void setUnitDao(UnitDao unitDao) {
		this.unitDao = (Hibernate3UnitDao) unitDao;
	}

	@Override
	public UnitDao getUnitDao() {
		if (mustProvideNewSession()) {
			unitDao.setSession(transactionManager.getSessionFactory().getCurrentSession());
		}

		return unitDao;
	}

	private boolean mustProvideNewSession() {
		return null == unitDao.getSession() || !unitDao.isSessionOpen();
	}

	public void setTransactionManager(HibernateTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public HibernateTransactionManager getTransactionManager() {
		return transactionManager;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(transactionManager, "transactionManager must be set");

		if (null == unitDao) {
			logger.info("No {} instance provided, will create one instance of {} for you", new String[]{//
					UnitDao.class.getName(),//
					Hibernate3UnitDao.class.getName()});
			unitDao = new Hibernate3UnitDao();
		}
	}
}
