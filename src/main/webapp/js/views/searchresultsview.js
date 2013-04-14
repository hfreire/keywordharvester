define([
  'jquery',
  'jquery.spin',
  'backbone.marionette',
  'vent',
  'bootstrap',
  'views/searchresultlistview',
  'views/searchresultgraphview'
], function($, Spinner, Marionette, vent){
	
	SearchResultsView = Marionette.Layout.extend({
		template: '#search-results-view',
		
        regions: {
            searchList: '#search-list',
            searchGraph: '#search-graph'
        },
	
		ui: {
			searchList: '#search-list',
			searchGraph: '#search-graph'
		},
		
		initialize : function() {
			var self = this;

            vent.on("search:start", function() {
				self.ui.searchList.spin('small', 'gray');
				self.ui.searchGraph.spin('small', 'gray');
            });
            
            vent.on("search:stop", function() {
            	self.ui.searchList.spin(false);
				self.ui.searchGraph.spin(false);
            });
            
        	vent.on('results:error', function(errorText) {
        		self.showError(errorText);
        	});
        	
            vent.on("results:show", function() {
            	self.showResults();
            });
			
		},
		
		showError: function() {
			this.ui.searchList.html("<h5>" + model.get('message') + "</h5>");
			this.ui.searchGraph.html("<h5>" + model.get('message') + "</h5>");
		},
		
		showResults: function() {
			this.searchList.show(new SearchResultListView({collection: this.model.get('relatedKeywords')}));
	    	this.searchGraph.show(new SearchResultGraphView({model: this.model}));
		}

	});
	
	return SearchResultsView;
});