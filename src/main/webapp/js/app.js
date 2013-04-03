define([
  'jquery', 
  'underscore', 
  'backbone',
  'models/keywordmodel',
  'views/searchboxview'
], function($, _, Backbone){
	
	Backbone.View.prototype.close = function() {
		if (this.beforeClose) {
			this.beforeClose();
		}
		this.remove();
		this.unbind();
	};

	var AppRouter = Backbone.Router.extend({
		routes : {
			"" : "showSearch",
			"search/:id" : "doSearch",
		},

		showView : function(selector, view) {
			if (this.currentView)
				this.currentView.close();
			
			$(selector).html(view.render().el);
			this.currentView = view;
			return view;
		},
		
		showSearch : function(callback) {
			app.currentView = app.showView('#search-box', new SearchBoxView({model: app.keyword}));
			if (callback)
				callback();
		},
		
		doSearch : function(id) {
			if(app.currentView) {
				$('input[placeholder=Search]').val(id);
				app.currentView.submitted();
			}
			else {
				this.showSearch(function() {
					$('input[placeholder=Search]').val(id);
					app.currentView.submitted();
				});
			}
		}
	});

	var initialize = function(){
		app = new AppRouter();
		app.keyword = new KeywordModel();
		Backbone.history.start();
		
		_.templateSettings = {
			    interpolate: /\<\@\=(.+?)\@\>/gim,
			    evaluate: /\<\@([\s\S]+?)\@\>/gim,
			    escape: /\<\@\-(.+?)\@\>/gim
		};
		
	};
	
	
  return { 
    initialize: initialize
  };
});