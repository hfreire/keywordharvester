package sh.exec.keywordharvester.service;

import sh.exec.keywordharvester.exception.NoRelatedKeywordsFoundException;
import sh.exec.keywordharvester.exception.UnableToHarvestKeywordException;
import sh.exec.keywordharvester.exception.UnknownKeywordHarvesterApiException;
import sh.exec.keywordharvester.model.KeywordModel;

public interface KeywordHarvesterService {
	public KeywordModel harvestRelatedKeywordsFromKeywordStringByApi(
			String keyword, String api) throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException, UnknownKeywordHarvesterApiException;
}
