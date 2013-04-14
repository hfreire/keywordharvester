define([
  'jquery',
  'backbone.marionette',
  'vent',
  'models/keywordmodel',
  'views/searchboxview',
  'views/searchresultsview'
], function($, Marionette, vent) {
    'use strict';
    
    var KeywordController = {};

    var Layout = Marionette.Layout.extend({
        template: '#content-view',

        regions: {
            'searchbox': '#search-box',
            'searchresults': '#search-results'
        }
    });

    var _keyword = new KeywordModel();
    
    KeywordController.emptySearch = function() {
    	KeywordController.layout = new Layout();
        
        KeywordController.layout.on("show", function() {
            vent.trigger("layout:rendered");
        });
        
        vent.trigger('app:show', KeywordController.layout);
	},
	
    vent.on("layout:rendered", function() {
    	KeywordController.layout.searchbox.show(new SearchBoxView({model: _keyword}));
    	KeywordController.layout.searchresults.show(new SearchResultsView({model: _keyword}));
    });
	
	KeywordController.search = function(keywordText) {
		var api = $("input:checked").val();
		
        if (keywordText.length > 0) {
    		if (_keyword.get('message'))
    			_keyword.unset('message');
        	
    		vent.trigger('search:start');
    		
            _keyword.set({id: keywordText}).fetch({
            	data: $.param({'api' : api}),
            	success: function () {
            		vent.trigger('keyword:success');
            	},
            	error: function () {
            		vent.trigger('keyword:error');
            	}
            });
            
			_keyword.set('relatedKeywords', '');
        }
        else {
            _keyword.set({id: null});
            vent.trigger('results:message', 'Type in a keyword to search!');
        }		
	}
	
	vent.on('keyword:success', function() {
		vent.trigger('search:stop');
		
		if (_keyword.get('relatedKeywords').size() > 0)
			vent.trigger('results:show');
		else
			vent.trigger('results:empty');
	});
	
	vent.on('keyword:error', function() {
		if (_keyword.get('message')) {			
			vent.trigger('results:error', _keyword.get('message'));
		} else {
			
		}
	});

    return KeywordController;
});