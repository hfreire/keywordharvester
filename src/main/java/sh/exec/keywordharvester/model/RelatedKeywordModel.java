package sh.exec.keywordharvester.model;

public class RelatedKeywordModel extends KeywordHarvesterModel {
	private KeywordModel keyword;
	private KeywordModel relatedKeyword;
	private double relevancy;
	
	public RelatedKeywordModel() {
		
	}
	
	public RelatedKeywordModel(KeywordModel keyword, double relevancy) {
		this.keyword = keyword;
		this.relevancy = relevancy;
	}
	
	public double getRelevancy() {
		return relevancy;
	}
	
	public RelatedKeywordModel setRelevancy(double relevancy) {
		this.relevancy = relevancy;
		return this;
	}

	public KeywordModel getRelatedKeyword() {
		return relatedKeyword;
	}

	public RelatedKeywordModel setRelatedKeyword(KeywordModel relatedKeyword) {
		this.relatedKeyword = relatedKeyword;
		return this;
	}

	public KeywordModel getKeyword() {
		return keyword;
	}

	public RelatedKeywordModel setKeyword(KeywordModel keyword) {
		this.keyword = keyword;
		return this;
	}

	@Override
	public String toString() {
		return "RelatedKeywordModel [keyword=" + keyword + ", relatedKeyword="
				+ relatedKeyword + ", relevancy=" + relevancy + "]";
	}
}
