package dao;



import helper.HibernateUtil;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * The GenericDAOImpl is  a helper  which implements basic CRUD methods for 
 * generic hibernate tables
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements
		GenericDAO<T, ID> {

	T type;
	private Class<T> elementClass;

	@SuppressWarnings("unchecked")
	public GenericDAOImpl() {
		elementClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected Session getSession() {
		return HibernateUtil.getSession();
	}

	public void save(T entity) {
		Session hibernateSession = this.getSession();
		hibernateSession.saveOrUpdate(entity);
		hibernateSession.flush();
	}

	public void merge(T entity) {
		Session hibernateSession = this.getSession();
		hibernateSession.merge(entity);
		hibernateSession.flush();
	}

	public void delete(T entity) {
		Session hibernateSession = this.getSession();
		hibernateSession.delete(entity);
		hibernateSession.flush();
	}

	public T load(ID entity) {
		Session hibernateSession = this.getSession();
		return (T) hibernateSession.get(this.elementClass, entity);
	}

	public List<T> findMany(Query query) {
		List<T> t;
		t = (List<T>) query.list();
		return t;
	}

	public T findOne(Query query) {
		T t;
		t = (T) query.uniqueResult();
		return t;
	}

	public T findByID(Class clazz, BigDecimal id) {
		Session hibernateSession = this.getSession();
		T t = null;
		t = (T) hibernateSession.get(clazz, id);
		return t;
	}

	public List findAll(Class clazz) {
		Session hibernateSession = this.getSession();
		List T = null;
		Query query = hibernateSession.createQuery("from " + clazz.getName());
		T = query.list();
		return T;
	}
}
