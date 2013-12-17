package net.cpollet.jixture.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.util.Assert;

/**
 * @author Christophe Pollet
 */
public class UnitDaoFactory implements InitializingBean {
	private final static Logger logger = LoggerFactory.getLogger(UnitDaoFactory.class);

	private HibernateTransactionManager transactionManager;

	private UnitDao unitDao;

	public void setUnitDao(UnitDao unitDao) {
		this.unitDao = unitDao;
	}

	public UnitDao getUnitDao() {
		if (mustProvideNewSession()) {
			unitDao.setSession(transactionManager.getSessionFactory().getCurrentSession());
		}

		return unitDao;
	}

	private boolean mustProvideNewSession() {
		return unitDao.getSession() == null || !unitDao.isSessionOpen();
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

		if (unitDao == null) {
			logger.info("No {} instance provided, will create one instance of {} for you", new String[] {
				UnitDao.class.getName(), SimpleUnitDao.class.getName()
			});
			unitDao = new SimpleUnitDao();
		}
	}
}
