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

package net.cpollet.jixture.asserts.helpers;

import net.cpollet.jixture.helper.MappingField;
import net.cpollet.jixture.tests.mappings.CartEntry;
import net.cpollet.jixture.tests.mappings.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Christophe Pollet
 */
public class TestMappingUtils {
	@Test
	public void toMapWithoutEmbeddedAttributesReturnsAMapOfNonNullMappingAttributes() throws NoSuchFieldException {
		// GIVEN
		User user = new User();
		user.setUsername("username");

		Collection<MappingField> fields = new ArrayList<MappingField>();
		fields.add(new MappingField(User.class.getDeclaredField("username")));
		fields.add(new MappingField(User.class.getDeclaredField("password")));

		// WHEN
		Map<String, ?> userMap = MappingUtils.toMap(user, fields);

		//THEN
		assertThat(userMap.get("username")).isEqualTo("username");
		assertThat(userMap.get("password")).isNull();
		assertThat(userMap).hasSize(2);
	}

	@Test
	public void toMapWithEmbeddedAttributesReturnsAMapOfNonNullMappingAttributes() throws NoSuchFieldException {
		// GIVEN
		CartEntry cartEntry = new CartEntry();
		cartEntry.setPk(new CartEntry.CartEntryPk());
		cartEntry.setCount(1);
		cartEntry.getPk().setClientId(2);
		cartEntry.getPk().setProductId(3);

		Collection<MappingField> fields = new ArrayList<MappingField>();
		fields.add(new MappingField(CartEntry.class.getDeclaredField("count")));
		fields.add(new MappingField(CartEntry.class.getDeclaredField("pk"), CartEntry.CartEntryPk.class.getDeclaredField("clientId")));
		fields.add(new MappingField(CartEntry.class.getDeclaredField("pk"), CartEntry.CartEntryPk.class.getDeclaredField("productId")));

		// WHEN
		Map<String, ?> cartEntryMap = MappingUtils.toMap(cartEntry, fields);

		//THEN
		assertThat(cartEntryMap.get("count")).isEqualTo(1);
		assertThat(cartEntryMap.get("pk.clientId")).isEqualTo(2);
		assertThat(cartEntryMap.get("pk.productId")).isEqualTo(3);
		assertThat(cartEntryMap).hasSize(3);
	}
}
