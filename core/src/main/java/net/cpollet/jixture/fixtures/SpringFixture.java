package net.cpollet.jixture.fixtures;

import java.util.Arrays;
import java.util.List;

public class SpringFixture implements Fixture {
	String context;
	List<Class<?>> classes;

	public SpringFixture(String context, Class<?>... classes) {
		this.context = context;
		this.classes = Arrays.asList(classes);
	}

	public SpringFixture(String context, List<Class<?>> classes) {
		this.context = context;
		this.classes = classes;
	}

	public String getContext() {
		return context;
	}

	public List<Class<?>> getClasses() {
		return classes;
	}

}
