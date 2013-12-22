package net.cpollet.jixture.fixtures.transformers;

import net.cpollet.jixture.fixtures.Fixture;
import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.fixtures.SpringFixture;
import net.cpollet.jixture.fixtures.XmlFileFixture;
import net.cpollet.jixture.helper.MappingField;
import net.cpollet.jixture.tests.mappings.CartEntry;
import net.cpollet.jixture.tests.mappings.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestSpringFixtureTransformer {
	private SpringFixtureTransformer springFixtureTransformer;

	@Before
	public void setUp() {
		springFixtureTransformer = new SpringFixtureTransformer();
	}

	@Test
	public void getFromTypeReturnXmlFileFixture() {
		// GIVEN

		// WHEN
		Class fromType = springFixtureTransformer.getFromType();

		// THEN
		assertThat(fromType).isEqualTo(SpringFixture.class);
	}

	@Test
	public void testTransform() throws NoSuchFieldException {
		// GIVEN
		SpringFixture springFixture = new SpringFixture("classpath:/tests/fixtures/spring-fixture.xml", User.class);

		// WHEN
		Fixture transformedFixture = springFixtureTransformer.transform(springFixture);

		// THEN
		assertThat(transformedFixture).isInstanceOf(Fixture.class);

		assertThat(transformedFixture.getObjects()).hasSize(1);
		assertThat(transformedFixture.getObjects().get(0)).isInstanceOf(User.class);
	}
}
