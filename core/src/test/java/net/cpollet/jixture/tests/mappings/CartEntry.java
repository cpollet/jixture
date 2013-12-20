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

package net.cpollet.jixture.tests.mappings;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Table;

/**
 * @author Christophe Pollet
 */
@Table(name = "cart_entry")
public class CartEntry {
	@EmbeddedId
	private CartEntryPk pk;

	@Column(name = "count")
	private Integer count;

	public CartEntryPk getPk() {
		return pk;
	}

	public void setPk(CartEntryPk pk) {
		this.pk = pk;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		CartEntry cartEntry = (CartEntry) o;

		if (count != null ? !count.equals(cartEntry.count) : cartEntry.count != null) {
			return false;
		}
		if (pk != null ? !pk.equals(cartEntry.pk) : cartEntry.pk != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = pk != null ? pk.hashCode() : 0;
		result = 31 * result + (count != null ? count.hashCode() : 0);
		return result;
	}

	@Embeddable
	public static class CartEntryPk {
		@Column(name = "client_id")
		protected Integer clientId;

		@Column(name = "product_id")
		protected Integer productId;

		public Integer getClientId() {
			return clientId;
		}

		public void setClientId(Integer clientId) {
			this.clientId = clientId;
		}

		public Integer getProductId() {
			return productId;
		}

		public void setProductId(Integer productId) {
			this.productId = productId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			CartEntryPk that = (CartEntryPk) o;

			if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) {
				return false;
			}
			if (productId != null ? !productId.equals(that.productId) : that.productId != null) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = clientId != null ? clientId.hashCode() : 0;
			result = 31 * result + (productId != null ? productId.hashCode() : 0);
			return result;
		}
	}
}
