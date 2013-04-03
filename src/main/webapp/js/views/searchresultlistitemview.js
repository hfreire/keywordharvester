define([
  'jquery', 
  'underscore', 
  'backbone-relational'
], function($, _, Backbone){
	
	window.SearchListItemView = Backbone.View.extend({
		tagName : 'tr',

		initialize : function() {
			this.template = _.template($('#search-list-item').html());
			this.model.bind("change", this.render, this);
			this.model.bind("destroy", this.close, this);
		},

		render : function(eventName) {
			$(this.el).html(this.template(this.model.toJSON()));
			return this;
		}
	});
	
	return window.SearchListItemView;

});