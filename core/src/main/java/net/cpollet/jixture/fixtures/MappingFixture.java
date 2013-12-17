package net.cpollet.jixture.fixtures;

import java.util.Arrays;
import java.util.List;

public class MappingFixture implements Fixture {
	List<Object> objects;

	public MappingFixture(Object... objects) {
		this.objects = Arrays.asList(objects);
	}

	public List<Object> getObjects() {
		return objects;
	}

}
