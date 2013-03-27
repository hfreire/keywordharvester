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
import sh.exec.keywordharvester.model.KeywordModel;
import sh.exec.keywordharvester.model.RelatedKeywordModel;
import sh.exec.keywordharvester.service.impl.VeryRelatedServiceImpl;
import sh.exec.keywordharvester.web.controller.api.KeywordController;

@RunWith(MockitoJUnitRunner.class)
public class KeywordControllerTest {
	@Mock
	private VeryRelatedServiceImpl veryRelatedService;

	@InjectMocks
	private KeywordController keywordController;

	@Test
	public void testGetRelatedKeywords()
			throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException {
		RelatedKeywordModel relatedKeyword = new RelatedKeywordModel();
		relatedKeyword.setKeyword(new KeywordModel("banana"));
		relatedKeyword.setRelevancy(0.67);

		KeywordModel keywordFromService = new KeywordModel("food");
		keywordFromService.getRelatedKeywords().add(relatedKeyword);

		Mockito.when(
				veryRelatedService
						.harvestRelatedKeywordsFromKeywordString(Mockito
								.any(String.class))).thenReturn(
				keywordFromService);

		KeywordModel keywordFromController = keywordController
				.getRelatedKeywords("food", "veryrelated");

		assertTrue(keywordFromController.getText().equals("food"));
		assertTrue(keywordFromController.getRelatedKeywords().get(0).getKeyword().getText().equals("banana"));
		assertTrue(keywordFromController.getRelatedKeywords().get(0)
				.getRelevancy() == 0.67);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = NoRelatedKeywordsFoundException.class)
	public void testGetRelatedKeywordsWithNoRelatedKeywords()
			throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException {
		Mockito.when(
				veryRelatedService
						.harvestRelatedKeywordsFromKeywordString(Mockito
								.any(String.class))).thenThrow(
										NoRelatedKeywordsFoundException.class);

		keywordController.getRelatedKeywords("food", "veryrelated");
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = UnableToHarvestKeywordException.class)
	public void testGetRelatedKeywordsWithUnableToHarvestKeyword()
			throws UnableToHarvestKeywordException,
			NoRelatedKeywordsFoundException {
		Mockito.when(
				veryRelatedService
						.harvestRelatedKeywordsFromKeywordString(Mockito
								.any(String.class))).thenThrow(
										UnableToHarvestKeywordException.class);

		keywordController.getRelatedKeywords("food", "veryrelated");
	}
}
