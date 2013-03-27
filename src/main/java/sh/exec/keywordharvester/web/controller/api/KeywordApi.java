package sh.exec.keywordharvester.web.controller.api;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sh.exec.keywordharvester.exception.NoRelatedKeywordsFoundException;
import sh.exec.keywordharvester.exception.UnableToHarvestKeywordException;
import sh.exec.keywordharvester.model.KeywordModel;
import sh.exec.keywordharvester.service.VeryRelatedService;

import javax.inject.Inject;

@Controller
@RequestMapping("/api/keyword")
public class KeywordApi {
	@Inject
	private VeryRelatedService veryRelatedService;
	
	@RequestMapping(value = "{text}", method = GET)
	@ResponseBody
	public KeywordModel getKeyword(@PathVariable String text) throws UnableToHarvestKeywordException, NoRelatedKeywordsFoundException {
		return veryRelatedService.harvestRelatedKeywordsFromKeywordString(text);
	}
	
	@ExceptionHandler(UnableToHarvestKeywordException.class)
	public KeywordModel onUnableToHarvestKeywordException(UnableToHarvestKeywordException e) {
		return (KeywordModel) e.getModel();
	}
	
	@ExceptionHandler(UnableToHarvestKeywordException.class)
	public KeywordModel onNoRelatedKeywordsFoundException(NoRelatedKeywordsFoundException e) {
		return (KeywordModel) e.getModel();
	}
}
