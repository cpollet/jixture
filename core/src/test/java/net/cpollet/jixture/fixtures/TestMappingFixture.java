package net.cpollet.jixture.fixtures;

import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestMappingFixture {
	@Test
	public void getObjectsReturnsAListWhenConstructedWithVarargs() {
		// GIVEN
		String fixture1 = "Fixture1";
		String fixture2 = "Fixture1";

		// WHEN
		MappingFixture mappingFixture = new MappingFixture(fixture1, fixture2);

		// THEN
		List<Object> fixtures = mappingFixture.getObjects();
		assertThat(fixtures)
				.hasSize(2)
				.containsSequence(fixture1, fixture2);
	}
}
