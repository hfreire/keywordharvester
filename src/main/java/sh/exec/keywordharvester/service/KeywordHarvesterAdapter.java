package sh.exec.keywordharvester.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import sh.exec.keywordharvester.exception.NoRelatedKeywordsFoundException;
import sh.exec.keywordharvester.exception.UnableToHarvestKeywordException;
import sh.exec.keywordharvester.exception.UnknownKeywordHarvesterApiException;
import sh.exec.keywordharvester.model.KeywordModel;

@Component
public class KeywordHarvesterAdapter implements KeywordHarvesterService {
	private Map<String, KeywordHarvesterApiService> apis;
	
	public Map<String, KeywordHarvesterApiService> getApis() {
		return apis;
	}

	public void setApis(Map<String, KeywordHarvesterApiService> apis) {
		this.apis = apis;
	}

	public KeywordHarvesterAdapter() {
		apis = new HashMap<String, KeywordHarvesterApiService>();
	}
	
	public KeywordModel harvestRelatedKeywordsFromKeywordStringByApi(
			String keyword, String api) throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException, UnknownKeywordHarvesterApiException {
		
		if (!apis.containsKey(api))
			throw new UnknownKeywordHarvesterApiException(api);
		else
			return apis.get(api).harvestRelatedKeywordsFromKeywordString(keyword);
	}
}
