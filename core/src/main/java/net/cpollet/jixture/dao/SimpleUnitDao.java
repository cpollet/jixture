package net.cpollet.jixture.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class SimpleUnitDao implements UnitDao {
	private Session session;

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getAll(Class<T> clazz) {
		return session.createCriteria(clazz).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOne(Class<T> clazz, Serializable id) {
		return (T) session.get(clazz, id);
	}

	@Override
	public void save(Object object) {
		session.save(object);
	}

	@Override
	public void delete(Object object) {
		session.delete(object);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void deleteAll(Class clazz) {
		session.evict(clazz);
		session.createQuery("delete from " + clazz.getName()).executeUpdate();
		flush();
	}

	@Override
	public void flush() {
		session.flush();
	}

	@Override
	public void clear() {
		session.clear();
	}

	@Override
	public void flushAndClear() {
		flush();
		clear();
	}

	@Override
	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public boolean isSessionOpen() {
		return session.isOpen();
	}

	@Override
	public SessionFactory getSessionFactory() {
		return getSession().getSessionFactory();
	}

	@Override
	public Set<String> getKnownMappings() {
		return getSessionFactory().getAllClassMetadata().keySet();
	}
}
