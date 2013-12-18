package net.cpollet.jixture.tests.mocks;

import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author Christophe Pollet
 */
public class TransactionTemplateMock extends TransactionTemplate {
	private boolean executed;

	@Override
	public <T> T execute(TransactionCallback<T> action) throws TransactionException {
		executed = true;
		return action.doInTransaction(null);
	}

	public boolean isExecuted() {
		return executed;
	}
}
