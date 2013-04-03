define([
  'jquery', 
  'underscore', 
  'backbone-relational',
  'views/searchresultlistitemview'
], function($, _, Backbone){

	window.SearchListView = Backbone.View.extend({
		tagName : 'table',

		className : 'table table-hover',

		initialize : function() {

			this.model.bind("reset", this.render, this);
			var self = this;
			this.model.bind("add", function(relatedKeyword) {
				$(self.el).append(new SearchListItemView({
					model : relatedKeyword
				}).render().el);
			});
		},

		render : function(eventName) {
			_.each(this.model.models, function(relatedKeyword) {
				$(this.el).append(new SearchListItemView({
					model : relatedKeyword
				}).render().el);
			}, this);
			return this;
		}
	});
	
	return window.SearchListView;
	
});
