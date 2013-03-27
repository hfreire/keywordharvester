package sh.exec.keywordharvester.exception;

import sh.exec.keywordharvester.model.KeywordHarvesterModel;

public class UnableToHarvestKeywordException extends KeywordHarvesterException {
	private static final long serialVersionUID = -7174931252092059285L;
	private static final String message = "Unable to harvest keyword";
	
	public UnableToHarvestKeywordException() {
		super(message);
	}
	
	public UnableToHarvestKeywordException(KeywordHarvesterModel model) {
		super(message, model);
	}
	
	public UnableToHarvestKeywordException(KeywordHarvesterModel model, Throwable throwable) {
		super(message, model, throwable);
	}
}
