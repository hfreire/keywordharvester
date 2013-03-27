package sh.exec.keywordharvester.exception;

import sh.exec.keywordharvester.model.KeywordHarvesterModel;

public class NoRelatedKeywordsFoundException extends KeywordHarvesterException {
	private static final long serialVersionUID = -7174931252092059285L;
	private static final String message = "No related keywords found";
	
	public NoRelatedKeywordsFoundException() {
		super(message);
	}
	
	public NoRelatedKeywordsFoundException(KeywordHarvesterModel model) {
		super(message, model);
	}
	
	public NoRelatedKeywordsFoundException(KeywordHarvesterModel model, Throwable throwable) {
		super(message, model, throwable);
	}
}
