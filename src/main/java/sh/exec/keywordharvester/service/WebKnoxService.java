package sh.exec.keywordharvester.service;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import sh.exec.keywordharvester.exception.NoRelatedKeywordsFoundException;
import sh.exec.keywordharvester.exception.UnableToHarvestKeywordException;
import sh.exec.keywordharvester.model.KeywordModel;

public interface WebKnoxService extends KeywordHarvesterApiService {
	public void fetchApiKeyFromWebPage() throws HttpException, IOException;
}
