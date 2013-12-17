package net.cpollet.jixture.tests.mappings;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author Christophe Pollet
 */
@Table(name = "clients")
public class Client {
	private String name;

	@Column(name = "name")
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Client client = (Client) o;

		if (name != null ? !name.equals(client.name) : client.name != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
