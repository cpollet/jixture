package net.cpollet.jixture.fixtures;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestSpringFixture {
	@Test
	public void getContextReturnsTheContextWhenConstructedWithVarargs() {
		// GIVEN
		SpringFixture springFixture = new SpringFixture("context", String.class);

		// WHEN + THEN
		assertThat(springFixture.getContext()).isEqualTo("context");
	}

	@Test
	public void getContextReturnsTheContextWhenConstructedWithList() {
		// GIVEN
		SpringFixture springFixture = new SpringFixture("context", Arrays.<Class<?>>asList(String.class));

		// WHEN + THEN
		assertThat(springFixture.getContext()).isEqualTo("context");
	}

	@Test
	public void getContextReturnsTheClassesListWhenConstructedWithVarargs() {
		// GIVEN
		SpringFixture springFixture = new SpringFixture("context", String.class, Integer.class);

		// WHEN
		List<Class<?>> classList = springFixture.getClasses();

		// THEN
		assertThat(classList)
				.hasSize(2)
				.containsSequence(String.class, Integer.class);
	}

	@Test
	public void getContextReturnsTheClassesListWhenConstructedWithList() {
		// GIVEN
		SpringFixture springFixture = new SpringFixture("context", Arrays.<Class<?>>asList(String.class, Integer.class));

		// WHEN
		List<Class<?>> classList = springFixture.getClasses();

		// THEN
		assertThat(classList)
				.hasSize(2)
				.containsSequence(String.class, Integer.class);
	}
}
