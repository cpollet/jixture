package net.cpollet.jixture.fixtures.loaders;

import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.SpringFixture;
import net.cpollet.jixture.tests.mappings.User;
import org.mockito.InjectMocks;

/**
 * @author Christophe Pollet
 */
public class TestSpringFixtureLoader extends AbstractTestFixtureLoader {
	@InjectMocks
	private SpringFixtureLoader springFixtureLoader;

	@Override
	FixtureLoader getFixtureLoader() {
		return springFixtureLoader;
	}

	@Override
	Fixture getLoadableFixture() {
		return new SpringFixture("classpath:/tests/fixtures/spring-fixture.xml", User.class);
	}

	@Override
	Fixture getNonLoadableFixture() {
		return new Fixture() {
		};
	}
}
