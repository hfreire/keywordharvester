define([
  'jquery', 
  'underscore', 
  'backbone.marionette',
], function($, _, Marionette){

	SearchListItemView = Marionette.ItemView.extend({
		template: "#search-list-item-view",
		tagName : 'tr',

	});
	
	SearchResultListView = Marionette.CompositeView.extend({
		template: "#search-list-view",
		itemViewContainer: "tbody",
		itemView: SearchListItemView,
		tagName: "table",
		className : 'table table-hover',

		initialize: function(){
	    },
		
		appendHtml: function(collectionView, itemView, index){
			collectionView.$("tbody").append(itemView.el);
		 },
	});
	
	return SearchResultListView;
	
});
