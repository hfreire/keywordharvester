package sh.exec.keywordharvester.repository;

import sh.exec.keywordharvester.model.KeywordHarvesterModel;

public interface KeywordHarvesterRepository<T extends KeywordHarvesterModel> {
	public void put(T t);
	public void delete(T t);
	public T get(T t);
}
