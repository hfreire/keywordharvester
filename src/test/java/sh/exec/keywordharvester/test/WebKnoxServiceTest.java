package sh.exec.keywordharvester.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import sh.exec.keywordharvester.exception.NoRelatedKeywordsFoundException;
import sh.exec.keywordharvester.exception.UnableToHarvestKeywordException;
import sh.exec.keywordharvester.model.KeywordModel;
import sh.exec.keywordharvester.service.impl.WebKnoxServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class WebKnoxServiceTest {
	@Mock
	private RestTemplate restTemplate;

	@Mock
	private HttpClient httpClient;

	@InjectMocks
	private WebKnoxServiceImpl webKnoxService;

	@Before
	public void beforeWebKnoxTest() throws ClientProtocolException, IOException {
		final String stringResponseBody = "<html>" +
				"<head></head>" +
				"<body>" +
				"<script>var apiKey = 'topSecretApiKey';</script>" +
				"</body>" +
				"</html>";

		HttpResponse httpResponse = new BasicHttpResponse(new BasicStatusLine(
				new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		httpResponse.setStatusCode(200);

		httpResponse.setEntity(new StringEntity(stringResponseBody));
		
		Mockito.when(httpClient.execute(Mockito.any(HttpGet.class)))
				.thenReturn(httpResponse);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testHarvestRelatedKeywordsFromKeywordString()
			throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException {
		String stringResponse = "[{" +
				"\"serpsBroad\": 49000000," +
				"\"searchedRelatedKeywords\": false," +
				"\"numberOfWords\": 5," +
				"\"ppcAdvertisers\": 0," +
				"\"keyword\": \"banana\"," +
				"\"monthlyExactSearches\": 1300," +
				"\"externalRequestCount\": 0," +
				"\"analyzeStep\": \"UNPROCESSED\"," +
				"\"serpsPhraseInUrl\": 40," +
				"\"serpsPhraseInTitle\": 58," +
				"\"id\": 271640" +
				"}]";
		
		Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.eq(String.class), Mockito.any(Map.class)))
		.thenReturn(stringResponse);

		KeywordModel keyword = webKnoxService
				.harvestRelatedKeywordsFromKeywordString("food");
		
    	assertTrue(keyword.getRelatedKeywords().get(0).getKeyword().getText().equals("banana"));
    	assertTrue(keyword.getRelatedKeywords().get(0).getRelevancy() == 0.65);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test(expected = NoRelatedKeywordsFoundException.class)  
    public void testHarvestRelatedKeywordsFromKeywordStringWithNoRelatedKeywordsFound() throws UnableToHarvestKeywordException, NoRelatedKeywordsFoundException {
    	Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.eq(String.class), Mockito.any(Map.class)))
		.thenThrow(HttpMessageNotReadableException.class);
    	
    	webKnoxService.harvestRelatedKeywordsFromKeywordString("food");
    }
	
	@SuppressWarnings("unchecked")
	@Test(expected = UnableToHarvestKeywordException.class)  
    public void testHarvestRelatedKeywordsFromKeywordStringWithUnableToHarvestKeyword() throws UnableToHarvestKeywordException, NoRelatedKeywordsFoundException {
    	Mockito.when(restTemplate.getForObject(Mockito.any(String.class), Mockito.eq(String.class), Mockito.any(Map.class)))
		.thenThrow(RestClientException.class);
    	
    	webKnoxService.harvestRelatedKeywordsFromKeywordString("food");
    }
}
