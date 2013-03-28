package sh.exec.keywordharvester.repository.impl;

import javax.inject.Inject;

import org.springframework.data.redis.core.RedisTemplate;

import sh.exec.keywordharvester.model.KeywordHarvesterModel;
import sh.exec.keywordharvester.repository.KeywordHarvesterRepository;

public class RedisKeywordHarvesterRepositoryImpl<T extends KeywordHarvesterModel> implements KeywordHarvesterRepository<T> {
	@Inject
	RedisTemplate<String, Object> redisTemplate;
	
	public void put(T t) {
		redisTemplate.opsForHash().put("", t.getKey(), t);
	}

	public void delete(T t) {
		redisTemplate.opsForHash().delete("", t.getKey());
	}

	@SuppressWarnings("unchecked")
	public T get(T t) {
		return (T) redisTemplate.opsForHash().get("", t.getKey());
	}
}
