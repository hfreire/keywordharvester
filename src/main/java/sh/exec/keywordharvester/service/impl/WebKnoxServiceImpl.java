package sh.exec.keywordharvester.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import sh.exec.keywordharvester.aspect.Loggable;
import sh.exec.keywordharvester.exception.NoRelatedKeywordsFoundException;
import sh.exec.keywordharvester.exception.UnableToHarvestKeywordException;
import sh.exec.keywordharvester.model.KeywordModel;
import sh.exec.keywordharvester.model.RelatedKeywordModel;
import sh.exec.keywordharvester.service.WebKnoxService;

@Service
public class WebKnoxServiceImpl implements WebKnoxService {
	
	@Inject
	private RestTemplate restTemplate;
	
	@Inject
	private HttpClient httpClient;

	@Value("${webknox.url}")
	private String url;
	
	@Value("${webknox.url.key}")
	private String urlKey = "";

	private String key = null;

	/**
	 *  cold start values (maximum observed values from previous experiments)
	 */
	private double maxCpc = 30.74;
	private int maxSerpsBroad = 1890000000;
	private int maxPpcAdvertisers = 50;
	private int maxMonthlyExactSearches = 673000;
	private int maxSerpsPhraseInUrl = 143000;
	private int maxMonthlyBroadSearches = 3350000;
	private int maxSerpsPhraseInTitle = 45500;
	private int maxSerpsPhrase = 89900000;
	
	@Loggable
	@Cacheable("webKnoxCache")
	public KeywordModel harvestRelatedKeywordsFromKeywordString(
			String stringKeyword) throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException {
		KeywordModel keyword = new KeywordModel(stringKeyword);

		try {
			if (key == null)
				fetchApiKeyFromWebPage();

			Map<String, String> vars = new HashMap<String, String>();
			vars.put("key", key);
			vars.put("keyword", keyword.getText());

			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;
			try {
				rootNode = mapper.readTree(restTemplate.getForObject(url,
						String.class, vars));
			} catch (RestClientException e) {
				/* if we get a 401, probably our key has expired
				 * so we need to fetch a new api key and retry the request
				 */
				if (e.getMessage().equals("401 Unauthorized")) {
					fetchApiKeyFromWebPage();
					vars.put("key", key);
					rootNode = mapper.readTree(restTemplate.getForObject(url,
							String.class, vars));
				}
			}

			double cpc = 0.0;
			int serpsBroad = 0;
			int ppcAdvertisers;
			int monthlyExactSearches;
			int serpsPhraseInUrl;
			int monthlyBroadSearches;
			int serpsPhraseInTitle;
			int serpsPhrase;
			double top10GoogleKeywordContainmentInHeadline = 0.0;
			double top10GoogleKeywordContainmentInTitle = 0.0;
			double top10GoogleKeywordContainmentInURL = 0.0;

			for (int i = 0; i < rootNode.size(); i++) {
				/* WebKnox related keyword API is known for returning the
				 * original keyword as a related keyword
				 */
				if (rootNode.path(i).path("keyword").asText()
						.equals(keyword.getText()))
					continue;

				RelatedKeywordModel relatedKeyword = new RelatedKeywordModel();
				relatedKeyword.setKeyword(new KeywordModel(rootNode.path(i)
						.path("keyword").asText()));

				/* parse all the necessary input to calculate keyword related
				 * relevancy
				 */
				if (rootNode.path(i).has("cpc")) {
					cpc = rootNode.path(i).path("cpc").asDouble();
					if (cpc > maxCpc)
						maxCpc = cpc;
				} else
					cpc = 0.0;

				if (rootNode.path(i).has("serpsBroad")) {
					serpsBroad = rootNode.path(i).path("serpsBroad").asInt();
					if (serpsBroad > maxSerpsBroad)
						maxSerpsBroad = serpsBroad;
				} else
					serpsBroad = 0;

				if (rootNode.path(i).has("ppcAdvertisers")) {
					ppcAdvertisers = rootNode.path(i).path("ppcAdvertisers")
							.asInt();
					if (ppcAdvertisers > maxPpcAdvertisers)
						maxPpcAdvertisers = ppcAdvertisers;
				} else
					ppcAdvertisers = 0;

				if (rootNode.path(i).has("monthlyExactSearches")) {
					monthlyExactSearches = rootNode.path(i)
							.path("monthlyExactSearches").asInt();
					if (monthlyExactSearches > maxMonthlyExactSearches)
						maxMonthlyExactSearches = monthlyExactSearches;
				} else
					monthlyExactSearches = 0;

				if (rootNode.path(i).has("serpsPhraseInUrl")) {
					serpsPhraseInUrl = rootNode.path(i)
							.path("serpsPhraseInUrl").asInt();
					if (serpsPhraseInUrl > maxSerpsPhraseInUrl)
						maxSerpsPhraseInUrl = serpsPhraseInUrl;
				} else
					serpsPhraseInUrl = 0;

				if (rootNode.path(i).has("monthlyBroadSearches")) {
					monthlyBroadSearches = rootNode.path(i)
							.path("monthlyBroadSearches").asInt();
					if (monthlyBroadSearches > maxMonthlyBroadSearches)
						maxMonthlyBroadSearches = monthlyBroadSearches;
				} else
					monthlyBroadSearches = 0;

				if (rootNode.path(i).has("serpsPhraseInTitle")) {
					serpsPhraseInTitle = rootNode.path(i)
							.path("serpsPhraseInTitle").asInt();
					if (serpsPhraseInTitle > maxSerpsPhraseInTitle)
						maxSerpsPhraseInTitle = serpsPhraseInTitle;
				} else
					serpsPhraseInTitle = 0;

				if (rootNode.path(i).has("serpsPhrase")) {
					serpsPhrase = rootNode.path(i).path("serpsPhrase").asInt();
					if (serpsPhrase > maxSerpsPhrase)
						maxSerpsPhrase = serpsPhrase;
				} else
					serpsPhrase = 0;

				top10GoogleKeywordContainmentInHeadline = 0.0;
				top10GoogleKeywordContainmentInTitle = 0.0;
				top10GoogleKeywordContainmentInURL = 0.0;

				if (rootNode.path(i).has("top10Google")) {
					JsonNode top10GoogleNode = rootNode.path(i).path(
							"top10Google");

					for (int j = 0; j < top10GoogleNode.size(); j++) {
						JsonNode top10GoogleSearchResultNode = top10GoogleNode
								.path(j);
						JsonNode keywordContainmentNode = top10GoogleSearchResultNode
								.get("keywordContainment");

						JsonNode relatedKeywordContainmentNode = keywordContainmentNode
								.get(relatedKeyword.getKeyword().getText());

						if (relatedKeywordContainmentNode.has("headline")
								&& relatedKeywordContainmentNode
										.get("headline").asBoolean())
							top10GoogleKeywordContainmentInHeadline++;

						if (relatedKeywordContainmentNode.has("title")
								&& relatedKeywordContainmentNode.get("title")
										.asBoolean())
							top10GoogleKeywordContainmentInTitle++;

						if (relatedKeywordContainmentNode.has("url")
								&& relatedKeywordContainmentNode.get("url")
										.asBoolean())
							top10GoogleKeywordContainmentInURL++;
					}
				}

				relatedKeyword.setRelevancy(calculateRelevancy(cpc, serpsBroad,
						ppcAdvertisers, monthlyExactSearches, serpsPhraseInUrl,
						monthlyBroadSearches, serpsPhraseInTitle, serpsPhrase,
						top10GoogleKeywordContainmentInHeadline,
						top10GoogleKeywordContainmentInTitle,
						top10GoogleKeywordContainmentInURL));

				keyword.getRelatedKeywords().add(relatedKeyword);
			}
			
			if (keyword.getRelatedKeywords().isEmpty())
				throw new NoRelatedKeywordsFoundException(keyword);

			Collections.sort(keyword.getRelatedKeywords(),
					new Comparator<RelatedKeywordModel>() {
						public int compare(RelatedKeywordModel k1,
								RelatedKeywordModel k2) {
							return Double.valueOf(k2.getRelevancy()).compareTo(
									k1.getRelevancy());
						}
					});

			return keyword;

		} catch (NullPointerException e) {
			throw new UnableToHarvestKeywordException(keyword, e);
		} catch (RestClientException e) {
			if (e.getMessage().equals("401 Unauthorized"))
				key = null;

			throw new UnableToHarvestKeywordException(keyword, e);
		} catch (HttpMessageNotReadableException e) {
			throw new NoRelatedKeywordsFoundException(keyword);
		} catch (ClientProtocolException e) {
			throw new UnableToHarvestKeywordException(keyword, e);
		} catch (IOException e) {
			throw new UnableToHarvestKeywordException(keyword, e);
		}
	}

	/**
	 * 
	 * @param cpc cost-per-click
	 * @param serpsBroad number of search results on google in broad mode
	 * @param ppcAdvertisers number of pay-per-click advertisers
	 * @param monthlyExactSearches number of exact searches per month
	 * @param serpsPhraseInUrl number of search results on google if searching in URLs
	 * @param monthlyBroadSearches number of broad searches per month
	 * @param serpsPhraseInTitle number of search results on google if searching in title
	 * @param serpsPhrase number of search on google in phrase mode
	 * @param top10GoogleKeywordContainmentInHeadline
	 * @param top10GoogleKeywordContainmentInTitle
	 * @param top10GoogleKeywordContainmentInURL
	 * @return a double meaning the relevancy of the related keyword
	 * 
	 * @description calculate the relevancy of a related keyword based on multiple
	 * parameters
	 */
	private double calculateRelevancy(
			double cpc,
			double serpsBroad,
			double ppcAdvertisers,
			double monthlyExactSearches,
			double serpsPhraseInUrl,
			double monthlyBroadSearches,
			double serpsPhraseInTitle,
			double serpsPhrase,
			double top10GoogleKeywordContainmentInHeadline,
			double top10GoogleKeywordContainmentInTitle,
			double top10GoogleKeywordContainmentInURL) {

		double relevancy = 0;

		/*
		 * we want the inverse: source Wikipedia: inexpensive ads
		 * that few people click on will have a low cost per 
		 * impression and a high cost per click
		 */
		cpc = 1 - normalize(cpc, 0, maxCpc);

		serpsBroad = normalize(serpsBroad, 0, maxSerpsBroad);
		ppcAdvertisers = normalize(serpsBroad, 0, maxPpcAdvertisers);
		monthlyExactSearches = normalize(monthlyExactSearches, 0,
				maxMonthlyExactSearches);
		serpsPhraseInUrl = normalize(serpsPhraseInUrl, 0, maxSerpsPhraseInUrl);
		monthlyBroadSearches = normalize(monthlyBroadSearches, 0,
				maxMonthlyBroadSearches);
		serpsPhraseInTitle = normalize(serpsPhraseInTitle, 0,
				maxSerpsPhraseInTitle);
		serpsPhrase = normalize(serpsPhrase, 0, maxSerpsPhrase);
		top10GoogleKeywordContainmentInHeadline = normalize(
				top10GoogleKeywordContainmentInHeadline / 10, 0, 1);
		top10GoogleKeywordContainmentInTitle = normalize(
				top10GoogleKeywordContainmentInTitle / 10, 0, 1);
		top10GoogleKeywordContainmentInURL = normalize(
				top10GoogleKeywordContainmentInURL / 10, 0, 1);

		/*System.out.println(cpc + "\n" + serpsBroad + "\n" + ppcAdvertisers
				+ "\n" + monthlyExactSearches + "\n" + serpsPhraseInUrl + "\n"
				+ monthlyBroadSearches + "\n" + serpsPhraseInTitle + "\n"
				+ serpsPhrase + "\n" + top10GoogleKeywordContainmentInHeadline
				+ "\n" + top10GoogleKeywordContainmentInTitle + "\n"
				+ top10GoogleKeywordContainmentInURL + "\n\n");*/

		relevancy = cpc * 0.65 + serpsBroad * 0.025 + ppcAdvertisers * 0.025
				+ monthlyExactSearches * 0.05 + serpsPhraseInUrl * 0.025
				+ monthlyBroadSearches * 0.025 + serpsPhraseInTitle * 0.025
				+ serpsPhrase * 0.025 + top10GoogleKeywordContainmentInHeadline
				* 0.05 + top10GoogleKeywordContainmentInTitle * 0.05
				+ top10GoogleKeywordContainmentInURL * 0.05;

		return roundToDecimals(relevancy, 2);
	}

	private double normalize(double value, double min, double max) {
		return (((value - (min)) / (max - (min))));
	}

	private double roundToDecimals(double d, int c) {
		int temp = (int) (d * Math.pow(10, c));
		return ((double) temp) / Math.pow(10, c);
	}

	/**
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @description a method for fetching a valid api key from webknox api webpage
	 */
	public void fetchApiKeyFromWebPage() throws ClientProtocolException, IOException {
		HttpResponse response = httpClient.execute(new HttpGet(urlKey));

		InputStream stream = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				stream, "ISO8859_8"));

		Pattern pattern = Pattern.compile("var apiKey = '(.+?)';");
		Matcher matcher = null;
		String line = null;
		while ((line = reader.readLine()) != null) {
			matcher = pattern.matcher(line);
			if (matcher.find()) {
				key = matcher.group(1);
				break;
			}
		}
	}
}
