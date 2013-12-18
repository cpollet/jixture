package net.cpollet.jixture.fixtures.loaders;

import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.tests.mappings.Client;
import net.cpollet.jixture.tests.mappings.User;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

/**
 * @author Christophe Pollet
 */
public class TestMappingFixtureLoader extends AbstractTestFixtureLoader {
	@InjectMocks
	private MappingsFixtureLoader mappingsFixtureLoader;

	@Override
	FixtureLoader getFixtureLoader() {
		return mappingsFixtureLoader;
	}

	@Override
	Fixture getLoadableFixture() {
		return new MappingFixture();
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
		mappingsFixtureLoader.load(new MappingFixture(//
				new User(),//
				new Client()), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).deleteAll(Client.class);
		inOrder.verify(unitDao).deleteAll(User.class);
	}

	@Test
	public void loadSavesEntitiesInOrder() {
		// GIVEN
		User user = new User();
		Client client = new Client();

		// WHEN
		mappingsFixtureLoader.load(new MappingFixture(user, client), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).save(user);
		inOrder.verify(unitDao).save(client);
	}

	@Test
	public void loadDeletesOldEntitiesBeforeSavingNewOnes() {
		// GIVEN
		User user = new User();

		// WHEN
		mappingsFixtureLoader.load(new MappingFixture(user), FixtureLoader.Mode.COMMIT);

		// WHEN
		InOrder inOrder = Mockito.inOrder(unitDao);
		inOrder.verify(unitDao).deleteAll(User.class);
		inOrder.verify(unitDao).save(user);
	}
}
