package com.juanan.photoManagement.data.dao;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Hibernate;
import org.hibernate.ejb.QueryHints;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.juanan.photoManagement.common.utils.NumberUtils;
import com.juanan.photoManagement.common.utils.StringUtils;
import com.juanan.photoManagement.data.entity.AbstractEntity;
import com.juanan.photoManagement.data.exception.DevelopmentException;
import com.juanan.photoManagement.data.exception.PhotoManagementDAOException;
import com.juanan.photoManagement.data.exception.PhotoManagementInfraestructureException;
import com.juanan.photoManagement.data.pagination.OrderParameters;
import com.juanan.photoManagement.data.pagination.Pagination;
import com.juanan.photoManagement.data.pagination.QueryParameters;
import com.juanan.photoManagement.data.pagination.QueryParameters.Operador;
import com.juanan.photoManagement.data.pagination.QueryParameters.Type;

@Scope("prototype")
public abstract class AbstractDAO<T extends AbstractEntity<TID>, TID> {

	protected Class<T> clazz;
	
	private final static String WHERE = "WHERE";
	
	@PersistenceContext(unitName = "back")
	protected EntityManager em;

	protected AbstractDAO(Class<T> clazz) {
		this.clazz = clazz;

	}

	protected EntityManager getEntityManager() {
		if (em == null) {
			throw new PhotoManagementInfraestructureException( "No se ha podido obtener el entityManager. ");
		}
		return em;
	}	
	
	@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
	public T select(TID id) throws PhotoManagementDAOException {
		EntityManager em = null;

		if (id == null) {
			throw new PhotoManagementDAOException("El id a consultar no puede ser null");
		}

		try {
			em = getEntityManager();
			T instance = em.find(clazz, id);
			return instance;

		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException(StringUtils.sustituir(
					"Error al consultar id={0}", id), e);
		}
	}

	/**
	 * 
	 * @param id
	 * @param methodInit son metodos Lazy a inicializar por defecto
	 * @return
	 * @throws PhotoManagementDAOException
	 */
	@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
	public T select(TID id, String methodInit) throws PhotoManagementDAOException {
		EntityManager em = null;
		T instance = null;

		try {

			em = getEntityManager();
			instance = em.find(clazz, id);

			// Para cada uno de los metodos, lo inicializo..
			if (methodInit != null && !methodInit.isEmpty()) {
				String[] metodos = methodInit.split(";");
				for (String metodo : metodos) {
					if (!metodo.trim().isEmpty()) {
						Method method = instance.getClass().getMethod(
								new StringBuffer("get").append(metodo.trim())
										.toString());
						if (method != null) {
							Hibernate.initialize(method.invoke(instance));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new PhotoManagementDAOException("Error al consultar id " + id
					+ " inicializando los metodos " + methodInit, e);
		}

		return instance;

	}
	

	@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
	public List<T> select(String consulta) throws PhotoManagementDAOException {

		EntityManager em = null;
		TypedQuery<T> q = null;
		try {

			em = getEntityManager();
			q = em.createQuery(consulta, clazz);

			return q.getResultList();
		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException("Error al listar", e);
		}
	}
	
	/**
	 * @param pagination
	 * @return Devuelve el listado de resultados segun los parametros de
	 *         paginacion. Los resultados a devolver son MaxResultados + 1. Ese
	 *         más uno es para controlar si es necesaria paginación o no. A la
	 *         hora de mostrar los resultados, mostrar uno menos si el número de
	 *         resultados es mayor que MaxResultados
	 * @throws PhotoManagementDAOException
	 */
	@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
	public List<T> select(Pagination pagination) throws PhotoManagementDAOException {
		return select(pagination, false);
	}

	
	/**
	 * @param pagination
	 * @param hace uso de la cache o no. Es necesario configurar adecuadamente el proyecto para usar la cache.
	 * @return Devuelve el listado de resultados segun los parametros de
	 *         paginacion. Los resultados a devolver son MaxResultados + 1. Ese
	 *         más uno es para controlar si es necesaria paginación o no. A la
	 *         hora de mostrar los resultados, mostrar uno menos si el número de
	 *         resultados es mayor que MaxResultados
	 * @throws PhotoManagementDAOException
	 */
	@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
	public List<T> select(Date timestamp) throws PhotoManagementDAOException {

		EntityManager entityManager = null;
		StringBuffer select = new StringBuffer(70);
		select.append("SELECT e FROM ");
		select.append(clazz.getName()).append(" e");
		select.append(" WHERE e.erased = 0");
		select.append(" AND e.timeStamp > :timestamp");
		List<T> results = null;

		try {
			entityManager = getEntityManager();
			TypedQuery<T> query = entityManager.createQuery(select.toString(), clazz);
			query.setParameter("timestamp", timestamp);
			results = query.getResultList();

		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException("Error al listar", e);
		}

		return results;
	}
	
	/**
	 * @param pagination
	 * @param hace uso de la cache o no. Es necesario configurar adecuadamente el proyecto para usar la cache.
	 * @return Devuelve el listado de resultados segun los parametros de
	 *         paginacion. Los resultados a devolver son MaxResultados + 1. Ese
	 *         más uno es para controlar si es necesaria paginación o no. A la
	 *         hora de mostrar los resultados, mostrar uno menos si el número de
	 *         resultados es mayor que MaxResultados
	 * @throws PhotoManagementDAOException
	 */
	@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
	public List<T> select(Pagination pagination, boolean cache) throws PhotoManagementDAOException {

		StringBuffer consulta = new StringBuffer("SELECT e FROM ").append(
				clazz.getName()).append(" e");
		List<T> res = null;

		try {

			// A cada parametro, le agrego el prefijo e.
			if (pagination.getSearch() != null) {
				for (QueryParameters query : pagination.getSearch()) {
					query.setField(new StringBuffer("e.").append(
							query.getField()).toString());
				}
			}

			if (pagination.getOrder() != null) {
				for (OrderParameters order : pagination.getOrder()) {
					order.setField(new StringBuffer("e.").append(
							order.getField()).toString());
				}
			}

			res = select(consulta.toString(), pagination, cache);

		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException("Error al listar", e);
		}

		return res;
	}

	/**
	 * @param consulta
	 * @param pagination
	 * @return Devuelve el listado de resultados segun los parametros de
	 *         paginacion. Los resultados a devolver son MaxResultados + 1. Ese
	 *         más uno es para controlar si es necesaria paginación o no. A la
	 *         hora de mostrar los resultados, mostrar uno menos si el número de
	 *         resultados es mayor que MaxResultados
	 * @throws PhotoManagementDAOException
	 * @see List<T> listar(Paginacion pagination)
	 */
	@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
	protected List<T> select(String consultaJPQL, Pagination pagination) throws PhotoManagementDAOException {
		return select(consultaJPQL, pagination, false);
	}
	
	/**
	 * @param consulta
	 * @param pagination
	 * @param hace uso de la cache o no. Es necesario configurar adecuadamente el proyecto para usar la cache.
	 * @return Devuelve el listado de resultados segun los parametros de
	 *         paginacion. Los resultados a devolver son MaxResultados + 1. Ese
	 *         más uno es para controlar si es necesaria paginación o no. A la
	 *         hora de mostrar los resultados, mostrar uno menos si el número de
	 *         resultados es mayor que MaxResultados
	 * @throws PhotoManagementDAOException
	 * @see List<T> listar(Paginacion pagination)
	 */
	@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
	protected List<T> select(String consultaJPQL, Pagination pagination, boolean cache)
			throws PhotoManagementDAOException {
		EntityManager entityManager = null;

		StringBuffer consulta = new StringBuffer(consultaJPQL);
		StringBuffer where = new StringBuffer(40);
		StringBuffer orderBy = new StringBuffer(20);
		TypedQuery<T> q = null;
		List<T> res = null;

		try {

			entityManager = getEntityManager();

			if (pagination.getWhere() != null) {
				if (!consultaJPQL.toUpperCase().contains(WHERE)) {
					where.append(" WHERE ").append(pagination.getWhere());
				} else {
					where.append(" AND ").append(pagination.getWhere());
				}
			}

			if (pagination.getOrderBy() != null) {
				orderBy.append(" ORDER BY ").append(pagination.getOrderBy());
			}

			if (where.length() != 0) {
				q = entityManager.createQuery(
						consulta.append(where).append(orderBy).toString(),
						clazz);

				int tamano = pagination.getSearch().size();
				for (int i = 0; i < tamano; i++) {
					QueryParameters parameter = pagination.getSearch().get(i);
					Type tipoDato = Type.parse(parameter.getType());

					switch (tipoDato) {
					case BIGDECIMAL:
					case FLOAT:
					case INT:
					case LONG:
						setNumericParameter(q, parameter, i + 1);
						break;
					case BOOLEAN:
						setBooleanParameter(q, parameter, i + 1);
						break;
					case DATE:
						setDateParameter(q, parameter, i + 1);
						break;
					case TEXT:
						setStringParameter(q, parameter, i + 1);
						break;
					case TEXT_NO_CASE_SENSITIVE:
						setStringNoCanseSensitiveParameter(q, parameter, i + 1);
						break;
					default:
						throw new DevelopmentException("Tipo no implementado: "
								+ parameter.getOperator());
					}
				}
			} else {
				q = entityManager.createQuery(consulta.append(orderBy).toString(), clazz);
			}

			if (pagination.getPagInicio() != null && pagination.getMaxResultados() != null) {
				q.setFirstResult(((pagination.getPagInicio() - 1) * pagination.getMaxResultados()));

				// Siempre devuelve uno mas para saber si necesitamos o no
				// paginacion.. A la hora de mostrar, mostrar uno menos.
				q.setMaxResults(pagination.getMaxResultados() + 1);
			}

			q.setHint(QueryHints.HINT_CACHEABLE, cache);
			res = q.getResultList();

		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException(
					"Error al listar con paginacion, pagination=" + pagination,
					e);
		}

		return res;
	}

	@Transactional(readOnly = true, propagation = Propagation.MANDATORY)
	public List<T> selectAll() throws PhotoManagementDAOException {
		List<T> returnList = null;
		EntityManager em = null;

		try {
			em = getEntityManager();

			StringBuffer jpql = new StringBuffer("SELECT e FROM ").append(
					clazz.getName()).append(" e");
			TypedQuery<T> query = em.createQuery(jpql.toString(), clazz);
			returnList = query.getResultList();

		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException("Error al listar todos", e);
		}

		return returnList;
	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public T insert(T instance) throws PhotoManagementDAOException {

		EntityManager em = null;
		try {
			em = getEntityManager();
			em.persist(instance);

			return instance;

		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException(
					"Error al insert dato tipo "
							+ ((instance != null && instance.getClass() != null) ? instance.getClass()
									: "null"), e);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public T update(T instance) throws PhotoManagementDAOException {

		try {
			EntityManager em = null;
			em = getEntityManager();
			em.merge(instance);
			
			return instance;

		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException("Error al update", e);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void delete(T instance) throws PhotoManagementDAOException {

		EntityManager em = null;

		try {

			em = getEntityManager();

			if (!em.contains(instance)) {
				instance = em.merge(instance);
			}

			em.remove(instance);

		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException("Error al eliminar", e);
		}

	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public int update(String updateJPQL, Pagination params)
			throws PhotoManagementDAOException {
		EntityManager em = null;
		Query q = null;

		try {

			em = getEntityManager();
			q = em.createQuery(updateJPQL);

			for (QueryParameters query : params.getSearch()) {
				q.setParameter(query.field, query.value);
			}

			return q.executeUpdate();

		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException("Error al actualizar", e);
		}
	}

	/**
	 * Ejecuta un procedimiento almacenado
	 * @param nombreProcedimiento
	 * @param pag
	 * @throws PhotoManagementDAOException
	 */
	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void executeStoredProcedure(String nombreProcedimiento, Pagination pag)
			throws PhotoManagementDAOException {
		EntityManager em = null;

		try {

			em = getEntityManager();

			Query q = em.createNativeQuery(nombreProcedimiento);

			for (QueryParameters query : pag.getSearch()) {
				q.setParameter(query.field, query.value);
			}
			q.executeUpdate();

		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException(
					"Error mientras se ejecutaba procedimiento almacendado", e);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.MANDATORY)
	public void ejecutarSql(String sqlString) throws PhotoManagementDAOException {
		EntityManager em = null;

		try {
			em = getEntityManager();
			Query q = em.createNativeQuery(sqlString);
			q.executeUpdate();
		} catch (PersistenceException e) {
			throw new PhotoManagementDAOException("Error al ejecutar sql nativo", e);
		}
	}

	
	
	public void flush() throws PhotoManagementDAOException {
		try {
			getEntityManager().flush();
		}
		catch (PersistenceException e) {
			throw new PhotoManagementDAOException(String.format("Error de persistencia: %s", e.getMessage()), e);
		}
	}

	public void clear() throws PhotoManagementDAOException {
		try {
			getEntityManager().clear();
		}
		catch (PersistenceException e) {
			throw new PhotoManagementDAOException(String.format("Error de persistencia: %s", e.getMessage()), e);
		}
	}
	
	// Metodos privados

	protected void setStringParameter(Query q, QueryParameters parameter,
			int position) {

		Operador operador = Operador.parse(parameter.getOperator());

		switch (operador) {
		case BEGIN_WITH:
		case NOT_BEGIN_WITH:
			q.setParameter(position, new StringBuffer(parameter.getValue())
					.append("%").toString());
			break;
		case CONTAINS:
		case NOT_CONTAINS:
			q.setParameter(
					position,
					new StringBuffer("%").append(parameter.getValue())
							.append("%").toString());
			break;
		case END_WITH:
		case NOT_END_WITH:
			q.setParameter(position,
					new StringBuffer("%").append(parameter.getValue())
							.toString());
			break;
		case IS:
		case EQUAL:
			q.setParameter(position, parameter.getValue());
			break;
		default:
			throw new DevelopmentException("Operador no implementado: "
					+ parameter.getOperator());
		}
	}

	protected void setStringNoCanseSensitiveParameter(Query q,
			QueryParameters parameter, int position) {

		Operador operador = Operador.parse(parameter.getOperator());

		switch (operador) {
		case BEGIN_WITH:
		case NOT_BEGIN_WITH:
			q.setParameter(position, new StringBuffer(parameter.getValue()
					.toLowerCase()).append("%").toString());
			break;
		case CONTAINS:
		case NOT_CONTAINS:
			q.setParameter(
					position,
					new StringBuffer("%")
							.append(parameter.getValue().toLowerCase())
							.append("%").toString());
			break;
		case END_WITH:
		case NOT_END_WITH:
			q.setParameter(
					position,
					new StringBuffer("%").append(
							parameter.getValue().toLowerCase()).toString());
			break;
		case IS:
		case EQUAL:
			q.setParameter(position, parameter.getValue().toLowerCase());
			break;
		default:
			throw new DevelopmentException("Operador no implementado: "
					+ parameter.getOperator());
		}
	}

	protected void setNumericParameter(Query q, QueryParameters parameter,
			int position) {

		Type tipoDato = Type.parse(parameter.getType());

		switch (tipoDato) {
		case INT:
			q.setParameter(position, Integer.valueOf(parameter.getValue()));
			break;
		case FLOAT:
			q.setParameter(position, Float.valueOf(parameter.getValue()));
			break;
		case LONG:
			q.setParameter(position, Long.valueOf(parameter.getValue()));
			break;
		case BIGDECIMAL:
			q.setParameter(position, Long.valueOf(parameter.getValue()));
			break;

		default:
			throw new DevelopmentException("El tipo no es numerico: "
					+ parameter.getType());
		}

	}

	protected void setBooleanParameter(Query q, QueryParameters parameter,
			int position) {

		Type tipoDato = Type.parse(parameter.getType());

		switch (tipoDato) {
		case BOOLEAN:
			q.setParameter(position, Boolean.valueOf(parameter.getValue()));
			break;

		default:
			throw new DevelopmentException("el tipo no es boolean: "
					+ parameter.getType());
		}
	}

	protected void setDateParameter(Query q, QueryParameters parameter,
			int position) throws PhotoManagementDAOException {
		long date = 0;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date fecha = null;

		if (NumberUtils.esNumero(parameter.getValue())) {
			date = Long.parseLong(parameter.getValue());
			q.setParameter(position, new Date(date));
		} else {
			try {
				fecha = format.parse(parameter.getValue());
				q.setParameter(position, fecha);
			} catch (ParseException e) {
				throw new PhotoManagementDAOException("Error al parsear la fecha " + parameter.getValue());
			}

		}
	} 
	
}
