package sh.exec.keywordharvester.exception;

import sh.exec.keywordharvester.model.KeywordHarvesterModel;

public abstract class KeywordHarvesterException extends Exception {
	private static final long serialVersionUID = 8505792266453522631L;
	private KeywordHarvesterModel model = null;
	
	public KeywordHarvesterException(String message) {
		super(message);
	}
	
	public KeywordHarvesterException(String message, KeywordHarvesterModel model) {
		super(message + ' ' + model);
		this.model = model;
	}
	
	public KeywordHarvesterException(String message, KeywordHarvesterModel model, Throwable throwable) {
		super(message + ' ' + model, throwable);
		this.model = model;
	}

	public KeywordHarvesterModel getModel() {
		return model;
	}

	public void setModel(KeywordHarvesterModel model) {
		this.model = model;
	}
}
