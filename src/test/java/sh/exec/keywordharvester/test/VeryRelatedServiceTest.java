package sh.exec.keywordharvester.test;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import sh.exec.keywordharvester.service.impl.VeryRelatedServiceImpl;
import sh.exec.keywordharvester.service.impl.veryrelated.ResultSet;
import sh.exec.keywordharvester.service.impl.veryrelated.ResultType;


@RunWith(MockitoJUnitRunner.class)
public class VeryRelatedServiceTest {
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private VeryRelatedServiceImpl veryRelatedService;
	
	@SuppressWarnings("unchecked")
	@Test
    public void testHarvestRelatedKeywordsFromKeywordString() throws UnableToHarvestKeywordException, NoRelatedKeywordsFoundException {
    	ResultType resultType = new ResultType();
    	resultType.setText("banana");
    	resultType.setHowRelated(9.00);
    	
    	List<ResultType> resultTypes = new LinkedList<ResultType>();
    	resultTypes.add(resultType);

    	ResultSet resultSet = new ResultSet();
    	resultSet.setResult(resultTypes);

    	Mockito.when(restTemplate.getForObject(Mockito.any(String.class),
    			Mockito.eq(ResultSet.class), Mockito.any(Map.class))).thenReturn(resultSet);
    	
    	KeywordModel keyword = veryRelatedService.harvestRelatedKeywordsFromKeywordString("food");
    	
    	assertTrue(keyword.getRelatedKeywords().get(0).getKeyword().getText().equals("banana"));
    	assertTrue(keyword.getRelatedKeywords().get(0).getRelevancy() == 0.67);
    }
	
	@SuppressWarnings("unchecked")
	@Test(expected = NoRelatedKeywordsFoundException.class)  
    public void testHarvestRelatedKeywordsFromKeywordStringWithNoRelatedKeywordsFound() throws UnableToHarvestKeywordException, NoRelatedKeywordsFoundException {
    	Mockito.when(restTemplate.getForObject(Mockito.any(String.class),
    			Mockito.eq(ResultSet.class), Mockito.any(Map.class))).thenThrow(HttpMessageNotReadableException.class);
    	
    	veryRelatedService.harvestRelatedKeywordsFromKeywordString("food");
    }
	
	@SuppressWarnings("unchecked")
	@Test(expected = UnableToHarvestKeywordException.class)  
    public void testHarvestRelatedKeywordsFromKeywordStringWithUnableToHarvestKeyword() throws UnableToHarvestKeywordException, NoRelatedKeywordsFoundException {
    	Mockito.when(restTemplate.getForObject(Mockito.any(String.class),
    			Mockito.eq(ResultSet.class), Mockito.any(Map.class))).thenThrow(RestClientException.class);
    	
    	veryRelatedService.harvestRelatedKeywordsFromKeywordString("food");
    }
}
