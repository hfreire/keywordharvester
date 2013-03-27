package sh.exec.keywordharvester.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import sh.exec.keywordharvester.exception.NoRelatedKeywordsFoundException;
import sh.exec.keywordharvester.exception.UnableToHarvestKeywordException;
import sh.exec.keywordharvester.model.KeywordModel;
import sh.exec.keywordharvester.model.RelatedKeywordModel;
import sh.exec.keywordharvester.service.VeryRelatedService;
import sh.exec.keywordharvester.service.impl.veryrelated.ResultSet;
import sh.exec.keywordharvester.service.impl.veryrelated.ResultType;

@Service
public class VeryRelatedServiceImpl implements VeryRelatedService {

	@Inject
	private RestTemplate restTemplate;
	
	@Value("${veryrelated.url}")
	private String url;
	
	@Value("${veryrelated.key}")
	private String key;
	
	public KeywordModel harvestRelatedKeywordsFromKeywordString(String stringKeyword) throws UnableToHarvestKeywordException, NoRelatedKeywordsFoundException {
		KeywordModel keyword = new KeywordModel(stringKeyword);
        
        try {
    		Map<String, String> vars = new HashMap<String, String>();
            vars.put("key", key);
            vars.put("keyword", keyword.getText());
        	
			ResultSet resultSet = restTemplate.getForObject(url, ResultSet.class, vars);
			
			for (ResultType resultType : resultSet.getResult()) {
				RelatedKeywordModel relatedKeyword = new RelatedKeywordModel(new KeywordModel(resultType.getText()), resultType.getHowRelated());
				keyword.getRelatedKeywords().add(relatedKeyword);
			}
        } catch (RestClientException e) {
        	throw new UnableToHarvestKeywordException(keyword, e);
        } catch (HttpMessageNotReadableException e ) {
        	throw new NoRelatedKeywordsFoundException(keyword);
        }

		return keyword;
	}
}

