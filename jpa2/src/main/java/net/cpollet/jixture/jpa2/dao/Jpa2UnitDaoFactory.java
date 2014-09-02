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

package net.cpollet.jixture.jpa2.dao;

import net.cpollet.jixture.dao.UnitDao;
import net.cpollet.jixture.dao.UnitDaoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.util.Assert;

/**
 * @author Christophe Pollet
 */
public class Jpa2UnitDaoFactory implements UnitDaoFactory, InitializingBean {
	private final static Logger logger = LoggerFactory.getLogger(Jpa2UnitDaoFactory.class);

	@Autowired
	@Qualifier(value = "jixture.core.transactionManager")
	private JpaTransactionManager transactionManager;

	private Jpa2UnitDao unitDao;

	@Override
	public void setUnitDao(UnitDao unitDao) {
		this.unitDao = (Jpa2UnitDao) unitDao;
	}

	@Override
	public UnitDao getUnitDao() {
		if (mustProvideNewEntitymanager()) {
			unitDao.setEntityManager(transactionManager.getEntityManagerFactory().createEntityManager());
		}

		return unitDao;
	}

	private boolean mustProvideNewEntitymanager() {
		return null == unitDao.getEntityManager() || !unitDao.isEntityManagerOpen();
	}

	public void setTransactionManager(JpaTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public JpaTransactionManager getTransactionManager() {
		return transactionManager;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(transactionManager, "transactionManager must be set");

		if (null == unitDao) {
			logger.info("No {} instance provided, will create one instance of {} for you", new String[]{//
					UnitDao.class.getName(),//
					Jpa2UnitDao.class.getName()});
			unitDao = new Jpa2UnitDao();
		}
	}
}
