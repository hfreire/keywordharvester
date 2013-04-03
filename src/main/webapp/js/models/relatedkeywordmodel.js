define([
  'jquery', 
  'underscore', 
  'backbone-relational'
], function($, _, Backbone){

	window.RelatedKeywordModel = Backbone.RelationalModel.extend({
	        defaults: {
	            "id": null,
	            "keyword": null,
	            "relevancy": null
	          },
	});
	
	return window.RelatedKeywordModel;
});