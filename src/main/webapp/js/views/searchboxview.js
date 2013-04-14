define([
  'jquery',
  'jquery.spin',
  'backbone.marionette',
  'vent',
  'bootstrap',
], function($, Spinner, Marionette, vent){

	SearchBoxView = Marionette.ItemView.extend({
		template: '#search-box-view',

		ui: {
			searchText: "input[placeholder=Search]",
			searchButton: "#search-button",
			searchDetails: '#search-details h5'
		},
		
		events : {
			"click #search-button" : "search",
		},
		
		initialize: function() {
			var self = this;
			var start, stop;

            vent.on("search:start", function() {
            	self.start = Date.now();
            	
            	self.ui.searchButton.button('loading');
				
				if (self.ui.searchDetails.html())
					self.ui.searchDetails.empty();
            });
            
            vent.on("search:stop", function() {
            	self.stop = Date.now();
            	
            	self.ui.searchButton.button('reset');
				
				self.ui.searchDetails.html(
					"Found " + 
					self.model.get('relatedKeywords').size() +
					" related keywords in " +
					(self.stop - self.start) / 1000 + 
					" seconds");
            });
        },
	
		search : function(event) {
			var keywordText = this.ui.searchText.val().trim();
			vent.trigger("search:api:keywordText", keywordText);
		}
	});
	
	vent.on("search:api:keywordText", function(keywordText) {
        Backbone.history.navigate("search/" + keywordText, {trigger: true});
    });
	
	return SearchBoxView;
});