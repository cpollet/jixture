/*
 * Copyright 2013 Christophe Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.cpollet.jixture.fixtures;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Christophe Pollet
 */
public class MappingFixture extends AbstractObjectFixture {
	List<Object> objects;

	public MappingFixture(Object... objectsToAdd) {
		objects = new LinkedList<Object>();
		addObjects(objectsToAdd);
	}

	@Override
	public Fixture addObjects(Object... objectsToAdd) {
		if (objectsToAdd.length > 0) {
			Collections.addAll(objects, objectsToAdd);
		}

		return this;
	}

	@Override
	public List<Object> getObjects() {
		return objects;
	}


}
