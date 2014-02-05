/*
 * Copyright 2014 Christophe Pollet
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
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Christophe Pollet
 */
public class CleaningFixture implements Fixture<Class> {
	Set<Class> classes;

	public CleaningFixture(Class... classesToDelete) {
		classes = new LinkedHashSet<Class>();
		addObjects(classesToDelete);
	}

	@Override
	public Fixture addObjects(Class... classesToDelete) {
		if (classesToDelete.length > 0) {
			Collections.addAll(classes, classesToDelete);
		}

		return this;
	}

	@Override
	public List<Class> getObjects() {
		return Collections.emptyList();
	}

	@Override
	public LinkedList<Class> getClassesToDelete() {
		return new LinkedList<Class>(classes);
	}
}