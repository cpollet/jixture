package net.cpollet.jixture.fixtures.loaders;

import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.fixtures.SpringFixture;
import net.cpollet.jixture.tests.mappings.Client;
import net.cpollet.jixture.tests.mappings.User;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

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

	@Test
	public void loadCleansEntitiesInReverseOrder() {
		// GIVEN

		// WHEN
		springFixtureLoader.load(new SpringFixture("classpath:/tests/fixtures/spring-fixture.xml", User.class, Client.class), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).deleteAll(Client.class);
		inOrder.verify(unitDao).deleteAll(User.class);
	}

	@Test
	public void loadSavesEntitiesInOrder() {
		// GIVEN

		// WHEN
		springFixtureLoader.load(new SpringFixture("classpath:/tests/fixtures/spring-fixture.xml", User.class, Client.class), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).save(Mockito.isA(User.class));
		inOrder.verify(unitDao).save(Mockito.isA(Client.class));
	}

	@Test
	public void loadDeletesOldEntitiesBeforeSavingNewOnes() {
		// GIVEN

		// WHEN
		springFixtureLoader.load(new SpringFixture("classpath:/tests/fixtures/spring-fixture.xml", User.class), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).deleteAll(User.class);
		inOrder.verify(unitDao).save(Mockito.any(User.class));
	}
}
