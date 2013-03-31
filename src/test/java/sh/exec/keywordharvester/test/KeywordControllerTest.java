package sh.exec.keywordharvester.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import sh.exec.keywordharvester.exception.NoRelatedKeywordsFoundException;
import sh.exec.keywordharvester.exception.UnableToHarvestKeywordException;
import sh.exec.keywordharvester.exception.UnknownKeywordHarvesterApiException;
import sh.exec.keywordharvester.model.KeywordModel;
import sh.exec.keywordharvester.model.RelatedKeywordModel;
import sh.exec.keywordharvester.service.KeywordHarvesterAdapter;
import sh.exec.keywordharvester.web.controller.api.KeywordController;

@RunWith(MockitoJUnitRunner.class)
public class KeywordControllerTest {
	@Mock
	private KeywordHarvesterAdapter keywordHarvesterAdapter;

	@InjectMocks
	private KeywordController keywordController;

	@Test
	public void testGetRelatedKeywordsByVeryRelatedApi()
			throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException, UnknownKeywordHarvesterApiException {
		RelatedKeywordModel relatedKeyword = new RelatedKeywordModel();
		relatedKeyword.setKeyword(new KeywordModel("banana"));
		relatedKeyword.setRelevancy(0.67);

		KeywordModel keywordFromService = new KeywordModel("food");
		keywordFromService.getRelatedKeywords().add(relatedKeyword);

		Mockito.when(
				keywordHarvesterAdapter
						.harvestRelatedKeywordsFromKeywordStringByApi(Mockito
								.any(String.class), Mockito.any(String.class))).thenReturn(
				keywordFromService);

		KeywordModel keywordFromController = keywordController
				.getRelatedKeywords("food", "veryrelated");

		assertTrue(keywordFromController.getText().equals("food"));
		assertTrue(keywordFromController.getRelatedKeywords().get(0).getKeyword().getText().equals("banana"));
		assertTrue(keywordFromController.getRelatedKeywords().get(0)
				.getRelevancy() == 0.67);
	}
	
	@Test
	public void testGetRelatedKeywordsByWebKnoxApi()
			throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException, UnknownKeywordHarvesterApiException {
		RelatedKeywordModel relatedKeyword = new RelatedKeywordModel();
		relatedKeyword.setKeyword(new KeywordModel("peanut"));
		relatedKeyword.setRelevancy(0.77);

		KeywordModel keywordFromService = new KeywordModel("food");
		keywordFromService.getRelatedKeywords().add(relatedKeyword);

		Mockito.when(
				keywordHarvesterAdapter
						.harvestRelatedKeywordsFromKeywordStringByApi(Mockito
								.any(String.class), Mockito.any(String.class))).thenReturn(
				keywordFromService);

		KeywordModel keywordFromController = keywordController
				.getRelatedKeywords("food", "webknox");

		assertTrue(keywordFromController.getText().equals("food"));
		assertTrue(keywordFromController.getRelatedKeywords().get(0).getKeyword().getText().equals("peanut"));
		assertTrue(keywordFromController.getRelatedKeywords().get(0)
				.getRelevancy() == 0.77);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = NoRelatedKeywordsFoundException.class)
	public void testGetRelatedKeywordsWithNoRelatedKeywords()
			throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException, UnknownKeywordHarvesterApiException {
		Mockito.when(
				keywordHarvesterAdapter
						.harvestRelatedKeywordsFromKeywordStringByApi(Mockito
								.any(String.class), Mockito.any(String.class))).thenThrow(
										NoRelatedKeywordsFoundException.class);

		keywordController.getRelatedKeywords("food", "veryrelated");
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = UnableToHarvestKeywordException.class)
	public void testGetRelatedKeywordsWithUnableToHarvestKeyword()
			throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException, UnknownKeywordHarvesterApiException {
		Mockito.when(
				keywordHarvesterAdapter
						.harvestRelatedKeywordsFromKeywordStringByApi(Mockito
								.any(String.class), Mockito.any(String.class))).thenThrow(
										UnableToHarvestKeywordException.class);

		keywordController.getRelatedKeywords("food", "veryrelated");
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = UnableToHarvestKeywordException.class)
	public void testGetRelatedKeywordsWithUnknownKeywordHarvesterApiException()
			throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException, UnknownKeywordHarvesterApiException {
		Mockito.when(
				keywordHarvesterAdapter
						.harvestRelatedKeywordsFromKeywordStringByApi(Mockito
								.any(String.class), Mockito.any(String.class))).thenThrow(
										UnableToHarvestKeywordException.class);

		keywordController.getRelatedKeywords("food", "garbage");
	}
}
