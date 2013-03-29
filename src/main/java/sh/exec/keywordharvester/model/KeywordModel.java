package sh.exec.keywordharvester.model;

import java.util.LinkedList;
import java.util.List;

public class KeywordModel extends KeywordHarvesterModel {
	private static final long serialVersionUID = 3121616016712359410L;
	private String text;
	private List<RelatedKeywordModel> relatedKeywords;

	public KeywordModel() {
		relatedKeywords = new LinkedList<RelatedKeywordModel>();
	}
	
	public KeywordModel(String text) {
		this.text = text;
		relatedKeywords = new LinkedList<RelatedKeywordModel>();
	}
	
	public String getText() {
		return text;
	}

	public KeywordModel setText(String text) {
		this.text = text;
		return this;
	}

	public List<RelatedKeywordModel> getRelatedKeywords() {
		return relatedKeywords;
	}

	public KeywordModel setRelatedKeywords(List<RelatedKeywordModel> relatedKeywords) {
		this.relatedKeywords = relatedKeywords;
		return this;
	}

	public String toString() {
		return "KeywordModel [text=" + text + " relatedKeywords=" + relatedKeywords.size() + "]";
	}

	public String getKey() {
		return text;
	}
}
