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

package net.cpollet.jixture.fixtures.generator.field;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestMultiFieldGenerator {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@SuppressWarnings("unchecked")
	@Test
	public void firstNextReturnsFirstFieldValueMap() {
		// GIVEN
		MultiFieldGenerator multiFieldGenerator = new MultiFieldGenerator();
		multiFieldGenerator.addFieldGenerator("field1", new Sequence(1, 2));
		multiFieldGenerator.addFieldGenerator("field2", new Sequence(3, 4));

		// WHEN
		Map<String, Object> values = (Map<String, Object>) multiFieldGenerator.next();

		// THEN
		assertThat(values).hasSize(2);
		assertThat(values.get("field1")).isEqualTo(1);
		assertThat(values.get("field2")).isEqualTo(3);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void secondNextReturnsSecondFieldValueMap() {
		// GIVEN
		MultiFieldGenerator multiFieldGenerator = new MultiFieldGenerator();
		multiFieldGenerator.addFieldGenerator("field1", new Sequence(1, 2));
		multiFieldGenerator.addFieldGenerator("field2", new Sequence(3, 4));

		multiFieldGenerator.next();

		// WHEN
		Map<String, Object> values = (Map<String, Object>) multiFieldGenerator.next();

		assertThat(values).hasSize(2);
		assertThat(values.get("field1")).isEqualTo(1);
		assertThat(values.get("field2")).isEqualTo(4);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void thirdNextReturnsThirdFieldValueMap() {
		// GIVEN
		MultiFieldGenerator multiFieldGenerator = new MultiFieldGenerator();
		multiFieldGenerator.addFieldGenerator("field1", new Sequence(1, 2));
		multiFieldGenerator.addFieldGenerator("field2", new Sequence(3, 4));

		multiFieldGenerator.next();
		multiFieldGenerator.next();

		// WHEN
		Map<String, Object> values = (Map<String, Object>) multiFieldGenerator.next();

		assertThat(values).hasSize(2);
		assertThat(values.get("field1")).isEqualTo(2);
		assertThat(values.get("field2")).isEqualTo(3);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void nextReturnsNextFieldValueMap() {
		// GIVEN
		MultiFieldGenerator multiFieldGenerator = new MultiFieldGenerator();
		multiFieldGenerator.addFieldGenerator("field1", new Sequence(1, 2));
		multiFieldGenerator.addFieldGenerator("field2", new Sequence(3, 4));
		multiFieldGenerator.addFieldGenerator("field3", new Sequence(5, 6));

		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>(8);

		for (int i = 0; 8 > i; i++) {
			values.add((Map) multiFieldGenerator.next());
		}

		assertThat(values).containsExactly(//
				buildMap("field1", 1, "field2", 3, "field3", 5), //
				buildMap("field1", 1, "field2", 3, "field3", 6), //
				buildMap("field1", 1, "field2", 4, "field3", 5), //
				buildMap("field1", 1, "field2", 4, "field3", 6), //
				buildMap("field1", 2, "field2", 3, "field3", 5), //
				buildMap("field1", 2, "field2", 3, "field3", 6), //
				buildMap("field1", 2, "field2", 4, "field3", 5), //
				buildMap("field1", 2, "field2", 4, "field3", 6));
	}

	@Test
	public void nextThrowsExceptionWhenNoMoreElementToGenerate() {
		// GIVEN
		MultiFieldGenerator multiFieldGenerator = new MultiFieldGenerator();
		multiFieldGenerator.addFieldGenerator("field1", new Sequence(1, 2));

		multiFieldGenerator.next();
		multiFieldGenerator.next();

		// WHEN + THEN
		expectedException.expect(NoSuchElementException.class);
		expectedException.expectMessage("No more field value to generate");

		multiFieldGenerator.next();
	}

	@Test
	public void hasNextReturnsFalseWhenThereAreNoMoreElementsToGenerate() {
		// GIVEN
		MultiFieldGenerator multiFieldGenerator = new MultiFieldGenerator();
		multiFieldGenerator.addFieldGenerator("field1", new Sequence(1, 2));

		multiFieldGenerator.next();
		multiFieldGenerator.next();

		// WHEN + THEN
		assertThat(multiFieldGenerator.hasNext()).isFalse();
	}

	@Test
	public void hasNextReturnsTrueOnlyWhenThereAreElementsToGenerate() {
		// GIVEN
		MultiFieldGenerator multiFieldGenerator = new MultiFieldGenerator();
		multiFieldGenerator.addFieldGenerator("field1", new Sequence(1, 2));
		multiFieldGenerator.addFieldGenerator("field2", new Sequence(3, 4));

		// WHEN + THEN
		multiFieldGenerator.next();
		assertThat(multiFieldGenerator.hasNext()).isTrue();

		multiFieldGenerator.next();
		assertThat(multiFieldGenerator.hasNext()).isTrue();

		multiFieldGenerator.next();
		assertThat(multiFieldGenerator.hasNext()).isTrue();

		multiFieldGenerator.next();
		assertThat(multiFieldGenerator.hasNext()).isFalse();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void resetResetsGenerators() {
		// GIVEN
		MultiFieldGenerator multiFieldGenerator = new MultiFieldGenerator();
		multiFieldGenerator.addFieldGenerator("field1", new Sequence(1, 2));
		multiFieldGenerator.addFieldGenerator("field2", new Sequence(3, 4));

		multiFieldGenerator.next();
		multiFieldGenerator.next();
		multiFieldGenerator.next();

		multiFieldGenerator.reset();

		Map<String, Object> values = (Map<String, Object>) multiFieldGenerator.next();

		assertThat(values).hasSize(2);
		assertThat(values.get("field1")).isEqualTo(1);
		assertThat(values.get("field2")).isEqualTo(3);
	}

	@Test
	public void currentReturnsLatestGeneratedElements() {
		// GIVEN
		MultiFieldGenerator multiFieldGenerator = new MultiFieldGenerator();
		multiFieldGenerator.addFieldGenerator("field1", new Sequence(1, 2));
		multiFieldGenerator.addFieldGenerator("field2", new Sequence(3, 4));

		Object expected = multiFieldGenerator.next();

		// WHEN
		Object actual = multiFieldGenerator.current();

		// THEN
		assertThat(expected).isSameAs(actual);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void currentCallsNextIfNeeded() {
		// GIVEN
		MultiFieldGenerator multiFieldGenerator = new MultiFieldGenerator();
		multiFieldGenerator.addFieldGenerator("field1", new Sequence(1, 2));
		multiFieldGenerator.addFieldGenerator("field2", new Sequence(3, 4));

		// WHEN + THEN
		Map<String, Object> values = (Map<String, Object>) multiFieldGenerator.current();
		assertThat(values).hasSize(2);
		assertThat(values.get("field1")).isEqualTo(1);
		assertThat(values.get("field2")).isEqualTo(3);

		values = (Map<String, Object>) multiFieldGenerator.next();
		assertThat(values).hasSize(2);
		assertThat(values.get("field1")).isEqualTo(1);
		assertThat(values.get("field2")).isEqualTo(4);
	}

	private Map<String, Object> buildMap(Object... elements) {
		Map<String, Object> map = new HashMap<String, Object>();

		for (int i = 0; i < elements.length; i = i + 2) {
			map.put((String) elements[i], elements[i+1]);
		}

		return map;
	}
}
