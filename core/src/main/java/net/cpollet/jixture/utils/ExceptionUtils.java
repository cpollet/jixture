package net.cpollet.jixture.utils;

/**
 * @author Christophe Pollet
 */
public class ExceptionUtils {
	public static RuntimeException wrapInRuntimeException(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}

		return new RuntimeException(e);
	}
}
