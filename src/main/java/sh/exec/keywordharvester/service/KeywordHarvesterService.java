package sh.exec.keywordharvester.service;

import sh.exec.keywordharvester.exception.NoRelatedKeywordsFoundException;
import sh.exec.keywordharvester.exception.UnableToHarvestKeywordException;
import sh.exec.keywordharvester.model.KeywordModel;

public abstract interface KeywordHarvesterService {
	public KeywordModel harvestRelatedKeywordsFromKeywordString(String keyword) throws UnableToHarvestKeywordException, NoRelatedKeywordsFoundException;
}
