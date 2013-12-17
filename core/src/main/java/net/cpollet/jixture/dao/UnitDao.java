package net.cpollet.jixture.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface UnitDao {
	public <T> List<T> getAll(Class<T> clazz);

	public <T> T getOne(Class<T> clazz, Serializable id);

	public void save(Object object);

	public void delete(Object object);

	@SuppressWarnings("rawtypes")
	public void deleteAll(Class clazz);

	public void flush();

	public void clear();

	public void flushAndClear();

	public void setSession(Session session);

	public Session getSession();

	public boolean isSessionOpen();

	public SessionFactory getSessionFactory();

	public Set<String> getKnownMappings();
}
