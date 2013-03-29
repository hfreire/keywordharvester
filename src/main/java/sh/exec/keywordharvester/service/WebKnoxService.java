package sh.exec.keywordharvester.service;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

public interface WebKnoxService extends KeywordHarvesterService {
	public void fetchApiKeyFromWebPage() throws HttpException, IOException;
}
