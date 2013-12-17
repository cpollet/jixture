package net.cpollet.jixture.fixtures;

/**
 * @author Christophe Pollet
 */
public interface FixtureTransformer<From> {
	Fixture transform(From fixture);
}
