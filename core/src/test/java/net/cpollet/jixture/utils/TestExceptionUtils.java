package net.cpollet.jixture.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Christophe Pollet
 */
public class TestExceptionUtils {
	@Test
	public void wrapInRuntimeExceptionDoesNotWrapExceptionIfAlreadyRuntimeExceptionInstance() {
		// GIVEN
		RuntimeException initialException = new RuntimeException();

		// WHEN
		Exception wrappedException = ExceptionUtils.wrapInRuntimeException(initialException);

		// THEN
		Assert.assertSame(initialException, wrappedException);
	}

	@Test
	public void wrapInRuntimeExceptionWrapsExceptionIfAlreadyRuntimeExceptionInstance() {
		// GIVEN
		Exception initialException = new Exception();

		// WHEN
		Exception wrappedException = ExceptionUtils.wrapInRuntimeException(initialException);

		// THEN
		Assert.assertSame(initialException, wrappedException.getCause());
	}
}
