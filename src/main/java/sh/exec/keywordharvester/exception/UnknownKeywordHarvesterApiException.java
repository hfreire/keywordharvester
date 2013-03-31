package sh.exec.keywordharvester.exception;

import sh.exec.keywordharvester.model.KeywordHarvesterModel;

public class UnknownKeywordHarvesterApiException extends KeywordHarvesterException {
	private static final long serialVersionUID = 2729458477908279905L;
	private static final String message = "Unknown api";
	
	public UnknownKeywordHarvesterApiException() {
		super(message);
	}
	
	public UnknownKeywordHarvesterApiException(String s) {
		super(message + ": " + s);
	}
	
	public UnknownKeywordHarvesterApiException(KeywordHarvesterModel model) {
		super(message, model);
	}
	
	public UnknownKeywordHarvesterApiException(KeywordHarvesterModel model, Throwable throwable) {
		super(message, model, throwable);
	}
}
