package net.cpollet.jixture.fixtures;

/**
 * @author Christophe Pollet
 */
public interface TransformableFixture extends Fixture {
	Fixture getTransformedFixture();
}
