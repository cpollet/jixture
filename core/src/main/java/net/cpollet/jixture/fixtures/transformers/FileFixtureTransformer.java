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

package net.cpollet.jixture.fixtures.transformers;

import net.cpollet.jixture.fixtures.FileFixture;
import net.cpollet.jixture.fixtures.MappingFixture;
import net.cpollet.jixture.fixtures.ObjectFixture;
import net.cpollet.jixture.helper.MappingBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.List;

/**
 * @author Christophe Pollet
 */
public abstract class FileFixtureTransformer<From extends FileFixture> implements FixtureTransformer<From, ObjectFixture> {
	@Autowired
	protected MappingBuilderFactory mappingBuilderFactory;

	@Override
	public ObjectFixture transform(From fixture) {
		List<Object> objects = parse(fixture);

		MappingFixture mappingFixture = new MappingFixture(objects.toArray());

		fixture.populateExtractionResult(objects);
		mappingFixture.setFilter(fixture.getFilter());

		return mappingFixture;
	}

	protected abstract List<Object> parse(From fixture);
}
