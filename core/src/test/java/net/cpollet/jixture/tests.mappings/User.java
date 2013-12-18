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
import javax.persistence.Table;

/**
 * @author Christophe Pollet
 */
@Table(name = "users")
public class User {
	@Column(name = "username")
	private String username;

	@Column
	private String password;

	private Integer connectionsCount;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setConnectionsCount(Integer connectionsCount) {
		this.connectionsCount = connectionsCount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		User user = (User) o;

		if (password != null ? !password.equals(user.password) : user.password != null) {
			return false;
		}
		if (username != null ? !username.equals(user.username) : user.username != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = username != null ? username.hashCode() : 0;
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}
}
