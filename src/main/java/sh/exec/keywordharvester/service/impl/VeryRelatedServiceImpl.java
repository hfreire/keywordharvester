package sh.exec.keywordharvester.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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

	@Value("${veryrelated.results}")
	private String results;

	// maximum HowRelated value observed by testing
	private final double observedMaxHowRelated = 140;
	// minimum HowRelated value observed by testing
	private final double observedMinHowRelated = -60;

	@Cacheable(value ="keyword")
	public KeywordModel harvestRelatedKeywordsFromKeywordString(
			String stringKeyword) throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException {
		KeywordModel keyword = new KeywordModel(stringKeyword);

		try {
			Map<String, String> vars = new HashMap<String, String>();
			vars.put("key", key);
			vars.put("results", results);
			vars.put("keyword", keyword.getText());

			ResultSet resultSet = restTemplate.getForObject(url,
					ResultSet.class, vars);

			double maxHowRelated = 0;
			double minHowRelated = 0;
			for (ResultType resultType : resultSet.getResult()) {
				RelatedKeywordModel relatedKeyword = new RelatedKeywordModel();
				relatedKeyword
						.setKeyword(new KeywordModel(resultType.getText()));
				// temporarily store HowRelated value
				relatedKeyword.setRelevancy(resultType.getHowRelated()); 

				if (maxHowRelated < resultType.getHowRelated())
					maxHowRelated = resultType.getHowRelated();
				if (minHowRelated > resultType.getHowRelated())
					minHowRelated = resultType.getHowRelated();

				keyword.getRelatedKeywords().add(relatedKeyword);
			}

			calculateRelevancyFromHowRelated(keyword.getRelatedKeywords(),
					maxHowRelated, minHowRelated);

			return keyword;
		} catch (RestClientException e) {
			throw new UnableToHarvestKeywordException(keyword, e);
		} catch (HttpMessageNotReadableException e) {
			throw new NoRelatedKeywordsFoundException(keyword);
		}
	}

	// calculate the relevancy for each related keyword by doing the average 
	// between the normalized HowRelated values from the current query results
	// and the normalized HowRelated values from previous observed query
	// results
	private void calculateRelevancyFromHowRelated(
			List<RelatedKeywordModel> relatedKeywords, double maxHowRelated,
			double minHowRelated) {
		for (RelatedKeywordModel relatedKeyword : relatedKeywords) {
			double adaptiveRelevancy = normalize(relatedKeyword.getRelevancy(),
					minHowRelated, maxHowRelated);
			double observedRelavancy = normalize(relatedKeyword.getRelevancy(),
					observedMinHowRelated, observedMaxHowRelated);

			relatedKeyword.setRelevancy(roundToDecimals(
					(adaptiveRelevancy + observedRelavancy) / 2, 2));
		}
	}

	private double normalize(double value, double min, double max) {
		return (((value - (min)) / (max - (min))));
	}

	private double roundToDecimals(double d, int c) {
		int temp = (int) (d * Math.pow(10, c));
		return ((double) temp) / Math.pow(10, c);
	}
}
