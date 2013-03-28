package sh.exec.keywordharvester.repository.impl;

import javax.inject.Inject;

import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.stereotype.Repository;

import sh.exec.keywordharvester.model.KeywordModel;
import sh.exec.keywordharvester.repository.KeywordRepository;

@Repository
public class KeywordRepositoryImpl implements KeywordRepository {
	@Inject
	DefaultRedisMap<String, KeywordModel> keywordRedisMap;
	
	public void put(KeywordModel keyword) {
		keywordRedisMap.put(keyword.getKey(), keyword);
	}

	public void delete(KeywordModel keyword) {
		keywordRedisMap.remove(keyword.getKey());
	}

	public KeywordModel get(KeywordModel keyword) {
		return (KeywordModel) keywordRedisMap.get(keyword.getKey());
	}
}
