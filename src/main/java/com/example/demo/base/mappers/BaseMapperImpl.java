package com.example.demo.base.mappers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.example.demo.base.mappers.common.Criteria;


@SuppressWarnings("unchecked")
public class BaseMapperImpl<T, PK> implements BaseMapper<T, PK> {

	@Autowired
	private SqlSession sqlSession;

	public List<T> getDataPageBy(Criteria criteria) {
		Assert.notNull(criteria, "[criteria] is required");
		Map<String, Object> map = getCriteriaMap(criteria);
		return sqlSession.selectList(getMapperFullName(GetDataPageBy), map);
	}

	public List<T> getDataBy(Criteria criteria) {
		Assert.notNull(criteria, "[criteria] is required");
		Map<String, Object> map = getCriteriaMap(criteria);
		return sqlSession.selectList(getMapperFullName(GetDataBy), map);
	}

	public T getById(PK id, boolean isForUpdate) {
		Assert.isTrue(isPKAvailabe, "table doesn't have primary key or more than 1 primary key");
		Assert.notNull(id, "id is required");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key_value", id);
		map.put("for_update", isForUpdate ? " for update " : "");
		return (T) sqlSession.selectOne(getMapperFullName(GetById), map);
	}

	public Long getTotalCountBy(Criteria criteria) {
		Assert.isTrue(isPKAvailabe, "table doesn't have primary key or more than 1 primary key");
		Assert.notNull(criteria, "[criteria] is required");
		Map<String, Object> map = getCriteriaMap(criteria);
		Number answer = (Number) sqlSession.selectOne(getMapperFullName(GetTotalCountBy), map);
		if (answer == null) {
			return 0L;
		}
		return answer.longValue();
	}

	public double getSumBy(Criteria criteria) {
		Assert.notNull(criteria, "[criteria] is required");
		Map<String, Object> map = getCriteriaMap(criteria);
		Double answer = (Double) sqlSession.selectOne(getMapperFullName(GetSumBy), map);
		clearCache();
		if (answer == null) {
			return 0.0;
		}
		return answer;
	}

	public List<T> getByIds(List<PK> ids, boolean isForUpdate) {
		Assert.isTrue(isPKAvailabe, "table doesn't have primary key or more than 1 primary key");
		Assert.notNull(ids, "id is required");
		Assert.notNull(isForUpdate, "isForupdate is required");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", ids);
		map.put("for_update", isForUpdate ? " for update " : "");
		return sqlSession.selectList(getMapperFullName(GetByIds), map);
	}

	public int insert(T entity) {
		Assert.isTrue(isPKAvailabe, "table doesn't have primary key or more than 1 primary key");
		Assert.notNull(entity, "[entity] is required");
		Integer result = (Integer) sqlSession.insert(getMapperFullName(INSERT), entity);
		clearCache();
		return result;
	}

	public int updateBy(T entity, Criteria criteria) {
		Assert.notNull(criteria, "[entity] is required");
		Map<String, Object> criteriaMap = getEntityCriteriaMap(entity, criteria);
		Integer result = sqlSession.update(getMapperFullName(UPDATEBY), criteriaMap);
		clearCache();
		return result;
	}

	public int update(T entity) {
		Assert.isTrue(isPKAvailabe, "table doesn't have primary key or more than 1 primary key");
		Assert.notNull(entity, "[entity] is required");
		Integer result = (Integer) sqlSession.update(getMapperFullName(UPDATE), entity);
		clearCache();
		return result;
	}

	public int deleteByIds(List<PK> ids) {
		Assert.isTrue(isPKAvailabe, "table doesn't have primary key or more than 1 primary key");
		Assert.notNull(ids, "ids is required");
		Integer result = (Integer) sqlSession.delete(getMapperFullName(DeleteByIds), ids);
		clearCache();
		return result;
	}

	public int deleteBy(Criteria criteria) {
		Assert.notNull(criteria, "[criteria] is required");
		Assert.isTrue(criteria.hasAddWhere(), "please add where into criteria");
		Map<String, Object> criteriaMap = getCriteriaMap(criteria);
		Integer result = (Integer) sqlSession.delete(getMapperFullName(DeleteBy), criteriaMap);
		clearCache();
		return result;
	}

	public int delete(PK id) {
		Assert.isTrue(isPKAvailabe, "table doesn't have primary key or more than 1 primary key");
		Integer result = (Integer) sqlSession.delete(getMapperFullName(DELETE), id);
		clearCache();
		return result;
	}

	/* 扩展方法 */

	public void clearCache() {
		sqlSession.clearCache();
	}

	public List<T> getAll() {
		return getDataBy(new Criteria());
	}

	public T getById(PK id) {
		return getById(id, false);
	}

	public List<T> getByIds(List<PK> ids) {
		return getByIds(ids, false);
	}

	public Long getTotalCount() {
		return getTotalCountBy(new Criteria());
	}

	public Long getTotalCountBy(String field, Object value) {
		Criteria criteria = new Criteria();
		criteria.addWhere(field + " = '%1$s'", new Object[] { value });
		return getTotalCountBy(criteria);
	}

	public T getOneBy(Criteria criteria) {
		Assert.notNull(criteria, "criteria is required");
		criteria.setLimit(1);
		List<T> result = getDataBy(criteria);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	public T getOneBy(String field, Object value) {
		Criteria criteria = new Criteria();
		criteria.addWhere(field + " = '%1$s'", new Object[] { value });
		return getOneBy(criteria);
	}

	public T getOneBy(String field, Object value, String orderByField, boolean isAsc) {
		Criteria criteria = new Criteria();
		criteria.addWhere(field + " = '%1$s'", new Object[] { value });
		setOrderForCriteria(criteria, orderByField, isAsc);
		return getOneBy(criteria);
	}

	public List<T> getDataBy(String field, Object value) {
		Criteria criteria = new Criteria();
		criteria.addWhere(field + " = '%1$s'", new Object[] { value });
		return getDataBy(criteria);
	}

	public List<T> getDataBy(String field, Object value, String orderByField, boolean isAsc) {
		Criteria criteria = new Criteria();
		criteria.addWhere(field + " = '%1$s'", new Object[] { value });
		setOrderForCriteria(criteria, orderByField, isAsc);
		return getDataBy(criteria);
	}

	public List<T> getDataPageBy(String field, Object value) {
		Criteria criteria = new Criteria();
		criteria.addWhere(field + " = '%1$s'", new Object[] { value });
		return getDataPageBy(criteria);
	}

	public List<T> getDataPageBy(String field, Object value, String orderByField, boolean isAsc) {
		Criteria criteria = new Criteria();
		criteria.addWhere(field + " = '%1$s'", new Object[] { value });
		setOrderForCriteria(criteria, orderByField, isAsc);
		return getDataBy(criteria);
	}

	public int insert(List<T> entitys) {
		Assert.notNull(entitys, "[entitys] is required");
		int answer = 0;
		for (T each : entitys) {
			answer += insert(each);
		}
		clearCache();
		return answer;
	}

	public int update(List<T> entitys) {
		Assert.isTrue(isPKAvailabe, "table doesn't have primary key or more than 1 primary key");
		Assert.notNull(entitys, "[entity] is required");
		int answer = 0;
		for (T each : entitys) {
			answer += update(each);
		}
		clearCache();
		return answer;
	}

	/* 将Criteria转成Map */
	protected Map<String, Object> getEntityCriteriaMap(Object obj, Criteria criteria) {
		Map<String, Object> map = new HashMap<String, Object>();
		updateCriteriaMap(criteria, map);
		map.put("entity", obj);
		return map;
	}

	protected Map<String, Object> getCriteriaMap(Criteria criteria) {
		Map<String, Object> map = new HashMap<String, Object>();
		updateCriteriaMap(criteria, map);
		return map;
	}

	private Map<String, Object> updateCriteriaMap(Criteria criteria, Map<String, Object> map) {
		if (criteria == null) {
			criteria = new Criteria();
		}
		map.putAll(criteria.getAnswer());
		return map;
	}

	/* End */

	/* 获得mapperId的命名空间全路径, 如果有Ex结尾的命名空间, 则优先调用 */
	protected String getMapperFullName(String mapperId) {
		String extendMapper = new StringBuilder(daoMappedId).append(".").append(mapperId).append("Ex").toString();
		if (sqlSession.getConfiguration().hasStatement(extendMapper)) {
			return extendMapper;
		}
		return new StringBuilder(daoMappedId).append(".").append(mapperId).toString();
	}

	private void setOrderForCriteria(Criteria criteria, String orderByField, boolean isAsc) {
		if (isAsc) {
			criteria.addAscOrder(orderByField);
		} else {
			criteria.addDescOrder(orderByField);
		}
	}

	public BaseMapperImpl() {
		daoMappedId = this.getClass().getName();
		Type pktype = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		isPKAvailabe = (pktype != Object.class);
	}

	protected String daoMappedId;
	private boolean isPKAvailabe = true;

	private static final String GetDataPageBy = "getDataPageBy";
	private static final String GetDataBy = "getDataBy";
	private static final String GetTotalCountBy = "getTotalCountBy";
	private static final String GetSumBy = "getSumBy";
	private static final String GetById = "getById";
	private static final String GetByIds = "getByIds";
	private static final String INSERT = "insert";
	private static final String UPDATE = "update";
	private static final String UPDATEBY = "updateBy";
	private static final String DELETE = "delete";
	private static final String DeleteByIds = "deleteByIds";
	private static final String DeleteBy = "deleteBy";

	

	/*
	 * private String getBondSql(String mapperId, Map<String, Object> map){
	 * return
	 * sqlSession.getConfiguration().getMappedStatement(getMapperFullName
	 * (mapperId)).getBoundSql(map).getSql(); }
	 */
}
